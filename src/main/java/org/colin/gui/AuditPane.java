package org.colin.gui;

import com.alee.extended.window.WebProgressDialog;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import org.colin.gui.graph.GraphView;
import org.colin.main.Main;
import org.colin.res.IconLoader;
import org.colin.util.ColourUtil;
import org.colin.visitors.MethodTreeVisitor;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;

import static org.colin.res.IconNames.ANNOTATION_ICON;
import static org.colin.res.IconNames.AST_DEPTH_ICON;
import static org.colin.res.IconNames.CLASS_ICON;

public class AuditPane extends JPanel implements TreeSelectionListener {
    private JTree methodTree;
    private DefaultTreeModel treeModel;
    private RSyntaxTextArea ta;

    private File workingFile;
    private CompilationUnit unit;

    private WebProgressDialog progressDialog;

    private ResourceBundle rb = ResourceBundle.getBundle(getClass().getSimpleName(), Main.locale);

    // FIX
    private JTabbedPane toolTabs;
    private GraphView graphView;
    // FIX

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

            final int beginOffset = ta.getSelectionStart(), endOffset = ta.getSelectionEnd();
            try {
                // get line offsets
                final int beginLine = ta.getLineOfOffset(beginOffset),
                        endLine = ta.getLineOfOffset(endOffset);

                // work out selection columns
                final int startLineOffset = ta.getLineStartOffset(beginLine);
                final int beginColumn = (beginOffset - startLineOffset),
                        endColumn = (endOffset - startLineOffset);

                // create selection range
                final Range selectionRange = Range.range((beginLine + 1), beginColumn, (endLine + 1), endColumn);

                // selectively depth-first search AST
                final Node rootNode = unit.getParentNodeForChildren();

                ArrayList<Object> trace = new ArrayList<>();

                Stack<Node> toVisit = new Stack<>();
                toVisit.push(rootNode);

                while(!(toVisit.empty())) {
                    final Node node = toVisit.pop();
                    final Range nodeRange = node.getRange().orElse(Range.range(0, 0, 0, 0));

                    if(nodeRange.contains(selectionRange)) {
                        String nodeType = node.getClass().toString();
                        nodeType = nodeType.substring(nodeType.lastIndexOf(".") + 1);
                        System.out.println(node.getClass().toString());
                        trace.add(nodeType);

                        for(final Node child : node.getChildNodes()) {
                            toVisit.push(child);
                        }
                    }

                }


                graphView.drawElements(trace);
                SwingUtilities.invokeLater(() -> graphView.repaint());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    public AuditPane(File sourceFile) {
        super(new BorderLayout());

        // read source file
        workingFile = sourceFile;

        System.out.println(sourceFile.getName());

        initComponents();
        initRenderers();
        readFile(workingFile);
        initAuditMenu();
/*
        String fileName = workingFile.getName();
        String className = fileName.substring(0, fileName.lastIndexOf("."));
        treeModel.setRoot(new ClassTreeNode(className));
*/

        progressDialog = new WebProgressDialog((JFrame)SwingUtilities.getRoot(this), "");
        progressDialog.setText(rb.getString("parsing_file"));
        progressDialog.setIndeterminate(true);
        progressDialog.getProgressBar().setStringPainted(true);
        progressDialog.getProgressBar().setString(rb.getString("parsing"));
        progressDialog.setVisible(true);

        /*
        Highlighter highlighter = ta.getHighlighter();
        Highlighter.HighlightPainter painter =
                new DefaultHighlighter.DefaultHighlightPainter(Color.pink);

        try {
            ta.getHighlighter().addHighlight(56, 78, painter);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }*/

        // parse source file
        new ParserWorker().execute();
    }

    private void readFile(File file) {
        try {
            ta.read(new FileReader(file), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        // assume class name from file name
        final String fileName = workingFile.getName();
        String className = fileName.substring(0, fileName.lastIndexOf("."));

        treeModel = new DefaultTreeModel(new ClassTreeNode(className));

        methodTree = new JTree();
        methodTree.setModel(treeModel);
        methodTree.addTreeSelectionListener(this);

        ta = new RSyntaxTextArea();
        ta.setAntiAliasingEnabled(true);
        ta.setRoundedSelectionEdges(true);
        ta.setEditable(false);
        ta.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Hack.ttf")).deriveFont(12f);
            ta.setFont(font);
            methodTree.setFont(font);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        RTextScrollPane sp = new RTextScrollPane(ta);

        Gutter gutter = sp.getGutter();
        gutter.setBookmarkIcon(IconLoader.loadIcon(ANNOTATION_ICON));
        gutter.setBookmarkingEnabled(true);
        gutter.setBackground(ColourUtil.fromHex("#f9f9f9"));

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

        toolTabs = new JTabbedPane();
        toolTabs.addTab(rb.getString("ast_depth"), IconLoader.loadIcon(AST_DEPTH_ICON), graphView = new GraphView());
        toolTabs.addTab("Blacks", IconLoader.loadIcon(CLASS_ICON), new JTable());
        JSplitPane horizonal = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, toolTabs);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(methodTree), horizonal);

        horizonal.setDividerLocation(0.8d);
        horizonal.setResizeWeight(0.8d);
        splitPane.setDividerLocation(200);

        add(splitPane, BorderLayout.CENTER);
    }

    private void initRenderers() {
        methodTree.setCellRenderer(new ClassTreeRenderer());
    }

    private void initAuditMenu() {
        JPopupMenu auditMenu = new JPopupMenu();
        auditMenu.add(new AuditAction());
        auditMenu.addSeparator();
        auditMenu.add("Run visitor...");
        ta.setPopupMenu(auditMenu);
        methodTree.setComponentPopupMenu(auditMenu);
    }

    private synchronized boolean initCompilationUnit() {
        try {
            unit = JavaParser.parse(workingFile);
        } catch (FileNotFoundException e) {
            JFrame parent = (JFrame) SwingUtilities.getRoot(this);
            JOptionPane.showMessageDialog(parent, "Problem parsing file!", "Parser error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (ParseProblemException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return false;
        }

        return true;
    }

    private void initTree() {
        // get root node for adding leaves (methods)
        ClassTreeNode root = (ClassTreeNode) treeModel.getRoot();

        // viist methods and append to root node
        new MethodTreeVisitor().visit(unit, root);

        // make top-level parent nodes' children visible
        expandTopLevelNodes(methodTree);
    }

    /**
     * Expand top-level JTree nodes
     *
     * @param tree tree being expanded
     */
    private void expandTopLevelNodes(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++)
            tree.expandRow(i);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        final Object node =
                methodTree.getLastSelectedPathComponent();

        if (node == null) return;

        if (node instanceof MethodTreeNode) {
            final MethodTreeNode method = (MethodTreeNode) node;

            // TODO:
            final MethodDeclaration methodDecl = method.getNode();
            final BlockStmt block = methodDecl.getBody().orElse(null);


            // try jump to line that node represents
            try {
                ta.setCaretPosition(ta.getLineStartOffset(method.getStartLine() - 1));
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

    }

}
