package org.colin.gui.views;

import com.alee.extended.window.WebProgressDialog;
import org.colin.gui.graph.GraphView;
import org.colin.gui.models.AuditModel;
import org.colin.main.Main;
import org.colin.res.IconLoader;
import org.colin.util.ColourUtil;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.ResourceBundle;

import static org.colin.res.IconNames.ANNOTATION_ICON;
import static org.colin.res.IconNames.AST_DEPTH_ICON;

public class AuditView extends JPanel {

    private JTree methodTree;
    private RSyntaxTextArea textArea;
    private WebProgressDialog progressDialog;
    private JTabbedPane toolTabs;
    private GraphView graphView;

    private AuditModel model;

    private ResourceBundle rb = ResourceBundle.getBundle(getClass().getSimpleName(), Main.locale);

    public AuditView(AuditModel model) {
        super(new BorderLayout());
        this.model = model;

        initComponents();
    }

    private void initComponents() {
        // TODO: get file name from model

        // initialise method tree model and listener
        methodTree = new JTree(new DefaultTreeModel(null));

        // initialise syntax textarea
        textArea = new RSyntaxTextArea();
        textArea.setAntiAliasingEnabled(true);
        textArea.setRoundedSelectionEdges(true);
        textArea.setEditable(false);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

        // attempt to load font resource for text area
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Hack.ttf")).deriveFont(12f);
            textArea.setFont(font);
            methodTree.setFont(font);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        // wrap textarea in scroll pane (enables line numbers)
        RTextScrollPane sp = new RTextScrollPane(textArea);

        Gutter gutter = sp.getGutter();
        gutter.setBookmarkIcon(IconLoader.loadIcon(ANNOTATION_ICON));
        gutter.setBookmarkingEnabled(true);
        gutter.setBackground(ColourUtil.fromHex("#f9f9f9"));

        toolTabs = new JTabbedPane();
        toolTabs.addTab(rb.getString("ast_depth"), IconLoader.loadIcon(AST_DEPTH_ICON), graphView = new GraphView());
        JSplitPane horizonal = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, toolTabs);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(methodTree), horizonal);

        horizonal.setDividerLocation(0.8d);
        horizonal.setResizeWeight(0.8d);
        splitPane.setDividerLocation(200);

        progressDialog = new WebProgressDialog((JFrame) SwingUtilities.getRoot(this), "");
        progressDialog.setText(rb.getString("parsing_file"));
        progressDialog.setIndeterminate(true);
        progressDialog.getProgressBar().setStringPainted(true);
        progressDialog.getProgressBar().setString(rb.getString("parsing"));

        add(splitPane, BorderLayout.CENTER);
    }

    public void setTreeListener(TreeSelectionListener listener) {
        methodTree.addTreeSelectionListener(listener);
    }

    public void jumpToLine(int line) throws BadLocationException {
        textArea.setCaretPosition(textArea.getLineStartOffset(line));
    }

    public GraphView getGraphView() {
        return graphView;
    }

    public RSyntaxTextArea getTextArea() {
        return textArea;
    }

    public JTree getMethodTree() {
        return methodTree;
    }

    public WebProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public JTabbedPane getToolTabs() {
        return toolTabs;
    }
}
