package org.colin.gui.views;

import com.alee.extended.window.WebProgressDialog;
import org.colin.gui.graph.AuditGraph;
import org.colin.gui.graph.GraphView;
import org.colin.gui.models.AuditModel;
import org.colin.main.Main;
import org.colin.res.IconLoader;
import org.colin.util.ColourUtil;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.annotation.Nonnull;
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
    // private GraphView graphView;

    private AuditGraph graphView;

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


        try {
            // attempt to load resource font for source code view
            Font font = Font.createFont(Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/Hack.ttf")).deriveFont(12f);

            textArea.setFont(font);
            methodTree.setFont(font);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, rb.getString("font_fail"));
        }

        // wrap textarea in scroll pane (enables line numbers)
        RTextScrollPane sp = new RTextScrollPane(textArea);

        Gutter gutter = sp.getGutter();
        gutter.setBookmarkIcon(IconLoader.loadIcon(ANNOTATION_ICON));
        gutter.setBookmarkingEnabled(true);
        gutter.setBackground(ColourUtil.fromHex("#f9f9f9"));

        toolTabs = new JTabbedPane();
        toolTabs.addTab(rb.getString("ast_depth"), IconLoader.loadIcon(AST_DEPTH_ICON), graphView = new AuditGraph());
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
        centreLineInView();
    }

    private void centreLineInView() throws BadLocationException {
        Rectangle rect = textArea.modelToView(textArea.getCaretPosition());
        Container container = SwingUtilities.getAncestorOfClass(JViewport.class, textArea);
        JViewport viewport = (JViewport) container;
        int extentHeight = viewport.getExtentSize().height;
        int viewHeight = viewport.getViewSize().height;

        int y = Math.max(0, rect.y - ((extentHeight - rect.height) / 2));
        y = Math.min(y, viewHeight - extentHeight);

        viewport.setViewPosition(new Point(0, y));
    }

    public AuditGraph getGraphView() {
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

    public String getLocalised(@Nonnull String key) {
        return rb.getString(key);
    }
}
