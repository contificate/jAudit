package org.colin.gui;

import com.alee.extended.window.WebProgressDialog;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.utils.ThreadUtils;
import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import org.colin.main.Main;
import org.colin.res.IconLoader;
import org.colin.visitors.MethodTreeVisitor;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.colin.res.IconNames.*;

public class AuditPane extends JPanel implements TreeSelectionListener {

    private WebToolBar toolBar;
    private JTree methodTree;
    private DefaultTreeModel treeModel;
    private RSyntaxTextArea ta;

    private File workingFile;
    private CompilationUnit unit;

    private WebProgressDialog progressDialog;

    private ResourceBundle rb = ResourceBundle.getBundle(getClass().getSimpleName(), Main.locale);

    private class ParserWorker extends SwingWorker<Boolean, Void> {
        @Override
        protected Boolean doInBackground() throws Exception {
            boolean success = initCompilationUnit();
            if (success) {
                initTree();
            }
            return success;
        }

        @Override
        protected void done() {
            // close progress dialog
            SwingUtilities.invokeLater(() -> {
                progressDialog.setVisible(false);
            });
        }
    }

    private class AuditAction extends AbstractAction {
        public AuditAction() {
            super("Create audit", IconLoader.loadIcon("annotation.png"));
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            try {
                FileWriter writer = new FileWriter("/home/dosto/report.html");

                for(Node n : unit.getChildNodes().get(unit.getChildNodes().size() - 1).getChildNodes()) {
                    Range range = n.getRange().orElse(null);
                    if(range != null) {
                        String start = (range.begin.line + ", " + range.begin.column);
                        String end = (range.begin.line + ", " + range.begin.column);
                        writer.write("<h2>" + start + " -> " + end + "</h2>");
                        writer.write(n.toString());
                        writer.write("<hr />");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
            final int start = ta.getSelectionStart(), end = ta.getSelectionEnd();

            System.out.println(start + " : " + end);


            try {
                Range meme = Range.range(ta.getLineOfOffset(start) - 1, 0, ta.getLineOfOffset(end) + 1, 0);

                System.out.println(ta.getLineOfOffset(start) + 1);

                for(Node n : unit.getChildNodes()) {
                    System.out.println("trying");
                    Optional<Range> nodeRange = n.getRange();
                    Range range = nodeRange.orElse(null);
                    if(range != null) {
                        if(range.contains(meme) || meme.contains(range)) {
                            System.err.println(n.toString());
                        }
                    } else System.out.println("range null");
                }

            } catch (BadLocationException e) {
                e.printStackTrace();
            }*/
        }
    }

    public AuditPane(File sourceFile) {
        super(new BorderLayout());

        initComponents();
        initRenderers();
        initAuditMenu();

        // read source file
        workingFile = sourceFile;
        readFile(workingFile);


        ClassMethodNode rootNode = new ClassMethodNode(ClassMethodNode.Type.CLASS, "Unknown", Range.range(0, 0, 0, 0));
        String fileName = workingFile.getName();
        String className = fileName.substring(0, fileName.lastIndexOf("."));
        rootNode.setName(className);
        treeModel.setRoot(rootNode);


        progressDialog = new WebProgressDialog(null, "");
        progressDialog.setText(rb.getString("parsing_file"));
        progressDialog.setIndeterminate(true);
        progressDialog.getProgressBar().setStringPainted(true);
        progressDialog.getProgressBar().setString(rb.getString("parsing"));
        progressDialog.setVisible(true);

        new ParserWorker().execute();
    }

    private void readFile(File file) {
        try {
            ta.read(new FileReader(file), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    private void initComponents() {
        toolBar = new WebToolBar();

        treeModel = new DefaultTreeModel(new ClassMethodNode(ClassMethodNode.Type.CLASS, "", Range.range(0, 0, 0, 0)));
        methodTree = new JTree();
        methodTree.setModel(treeModel);
        methodTree.addTreeSelectionListener(this);

        ta = new RSyntaxTextArea();
        ta.setFont(new Font("Hack", Font.PLAIN, 12)); // TODO: load font resource
        ta.setAntiAliasingEnabled(true);

        ta.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

        RTextScrollPane sp = new RTextScrollPane(ta);

        Gutter gutter = sp.getGutter();
        gutter.setBookmarkIcon(IconLoader.loadIcon(ANNOTATION_ICON));
        gutter.setBookmarkingEnabled(true);
        gutter.setToolTipText("Juden");
        gutter.setBackground(hex2Rgb("#f9f9f9"));


        /*
        Theme theme;
        try {
            theme = Theme.load(getClass().getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
            theme.apply(ta);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        /*
        try {
            gutter.addLineTrackingIcon(3, IconLoader.loadIcon("jvm-icon.png"));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }*/

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(methodTree), sp);
        splitPane.setDividerLocation(200);


        //sadd(toolBar, BorderLayout.PAGE_START);

        add(splitPane, BorderLayout.CENTER);
    }

    private void initRenderers() {
        methodTree.setCellRenderer(new ClassTreeRenderer());
    }

    private void initAuditMenu() {
        JPopupMenu auditMenu = new JPopupMenu();
        auditMenu.add("Create audit");
        auditMenu.add(new AuditAction());
        auditMenu.addSeparator();
        auditMenu.add("Run visitor...");
        ta.setPopupMenu(auditMenu);
        methodTree.setComponentPopupMenu(auditMenu);
    }

    private boolean initCompilationUnit() {

        try {
            unit = JavaParser.parse(workingFile);
        } catch (FileNotFoundException e) {
            JFrame parent = (JFrame) SwingUtilities.getRoot(this);
            JOptionPane.showMessageDialog(parent, "Problem parsing file!", "Parser error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void initTree() {
        ClassMethodNode root = (ClassMethodNode) treeModel.getRoot();

        // viist methods and append to root node
        new MethodTreeVisitor().visit(unit, root);

        // make top-level parent nodes' children visible
        expandTopLevelNodes(methodTree);

        // TEST

        TokenRange range = (unit.getTokenRange().isPresent() ? unit.getTokenRange().get() : null);
        if(range != null) {
            for(JavaToken meme : range) {
                if(!TokenTypes.isWhitespace(meme.getKind())) {
                    System.out.println(meme.getText());
                    System.out.println(meme.getKind());
                }
            }
        }



        // TEST
    }

    /**
     * Expand top-level JTree nodes
     * @param tree tree being expanded
     */
    private void expandTopLevelNodes(JTree tree) {
       for(int i = 0; i < tree.getRowCount(); i++)
           tree.expandRow(i);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        // get selected node
        ClassMethodNode node = (ClassMethodNode)
                methodTree.getLastSelectedPathComponent();

        if (node == null) return;

        // try jump to line that node represents
        try {
            ta.setCaretPosition(ta.getLineStartOffset(node.getStartLine() - 1));
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

}
