package org.colin.gui.controllers;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import org.colin.audit.AuditContext;
import org.colin.gui.ClassTreeNode;
import org.colin.gui.ClassTreeRenderer;
import org.colin.gui.MethodTreeNode;
import org.colin.gui.graph.AuditGraph;
import org.colin.gui.models.AuditModel;
import org.colin.gui.models.AuditorModel;
import org.colin.gui.views.AuditView;
import org.colin.gui.views.AuditorView;
import org.colin.res.IconLoader;
import org.colin.visitors.MethodTreeVisitor;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import static org.colin.res.IconNames.AST_DEPTH_ICON;
import static org.colin.res.IconNames.TREE_ICON;

public class AuditController implements TreeSelectionListener, CaretListener {

    /**
     * Model used for updating the view.
     */
    private AuditModel model;

    /**
     * Auditing view used to invoke controller.
     */
    private AuditView view;

    /**
     * Create audit controller to cohere both model and view.
     *
     * @param model model that updates what's shown by {@link AuditView}
     * @param view  view that invokes controller's actions
     */
    public AuditController(AuditModel model, AuditView view) {
        this.model = model;
        this.view = view;

        initRenderers();
        initAuditMenu();
        initListeners();

        showProgressDialog(true);
        readFile();

        new ParserWorker().execute();
    }

    private void initRenderers() {
        view.getMethodTree().setCellRenderer(new ClassTreeRenderer());
    }

    private void initListeners() {
        view.setTreeListener(this);
        view.getTextArea().addCaretListener(this);
    }

    @Override
    public void caretUpdate(CaretEvent caretEvent) {
        System.out.println("faggot");
    }

    /**
     * Create compilation unit (parse file) in background
     * as to avoid blocking the GUI thread.
     */
    private class ParserWorker extends SwingWorker<Boolean, Void> {
        @Override
        protected Boolean doInBackground() throws ParseProblemException {
            // attempt to parse file
            boolean success = initCompilationUnit();

            // if parsing is successful, run method visitor to initialise method-tree
            if (success) {
                initTree();
            }

            // if parsing failed without throwing ParseProblemException, return success value anyway
            return success;
        }

        /**
         * Parsing complete callback, closes progress dialog.
         */
        @Override
        protected void done() {
            // queue progress-dialog closure in AWT event queue
            SwingUtilities.invokeLater(() -> showProgressDialog(false));
        }
    }

    /**
     * Action performed when an audit is intended to be created.
     */
    private class ViewDepthAction extends AbstractAction {

        protected AuditContext contextTrace;

        public ViewDepthAction() {
            super(view.getLocalised("view_depth"), IconLoader.loadIcon(AST_DEPTH_ICON));
            putValue("MnemonicKey", KeyEvent.VK_V);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            // get source code view
            final RSyntaxTextArea textArea = view.getTextArea();

            // get rough selection bounds
            final int beginOffset = textArea.getSelectionStart(),
                    endOffset = textArea.getSelectionEnd();

            try {
                // get line offsets
                final int beginLine = textArea.getLineOfOffset(beginOffset),
                        endLine = textArea.getLineOfOffset(endOffset);

                // work out selection columns
                int startLineOffset = textArea.getLineStartOffset(beginLine);
                int beginColumn = (beginOffset - startLineOffset),
                        endColumn = (endOffset - startLineOffset);

                // single line selections will likely be snapped selections so we reduce the selection to get more accurate positions
                if (beginLine == endLine) {
                    beginColumn++;
                    endColumn--;
                }

                // create selection range
                final Range selectionRange = Range.range((beginLine + 1), beginColumn, (endLine + 1), endColumn);

                // selectively depth-first search AST
                final Node rootNode = model.getUnit().getParentNodeForChildren();

                // initialise new context
                contextTrace = new AuditContext();

                // AST traversal stack
                Stack<Node> toVisit = new Stack<>();
                toVisit.push(rootNode);

                // continually pop and traverse and push all childern of interior nodes
                while (!(toVisit.empty())) {
                    final Node node = toVisit.pop();
                    final Range nodeRange = node.getRange().orElse(Range.range(0, 0, 0, 0));

                    // internal node contains selection range?
                    if (nodeRange.contains(selectionRange)) {
                        // add internal node to context-trace
                        contextTrace.add(node);

                        // push all children
                        for (final Node child : node.getChildNodes()) {
                            toVisit.push(child);
                        }
                    }

                }

                // set the context of the graph view
                AuditGraph graphView = view.getGraphView();
                graphView.setContext(contextTrace);

                // queue graph view repaint
                SwingUtilities.invokeLater(graphView::repaint);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    private class AuditAction extends ViewDepthAction {

        public AuditAction() {
            putValue("Name", view.getLocalised("create_audit"));
            putValue("SmallIcon", IconLoader.loadIcon(TREE_ICON));
            putValue("MnemonicKey", KeyEvent.VK_A);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            super.actionPerformed(actionEvent);

            SwingUtilities.invokeLater(() -> {
                AuditorModel model = new AuditorModel(contextTrace);
                AuditorView view = new AuditorView(null);
                AuditorController controller = new AuditorController(model, view);
                view.setVisible(true);
            });

        }
    }

    private void initAuditMenu() {
        JPopupMenu auditMenu = new JPopupMenu();
        auditMenu.add(new ViewDepthAction());
        auditMenu.addSeparator();
        auditMenu.add(new AuditAction());
        view.getTextArea().setPopupMenu(auditMenu);
        view.getMethodTree().setComponentPopupMenu(auditMenu);
    }

    /**
     * Initialise method-tree using {@link MethodTreeVisitor}
     */
    private void initTree() {
        // get tree model
        final DefaultTreeModel modelTree = model.getTreeModel();

        // get root node for adding leaves (methods)
        ClassTreeNode root = (ClassTreeNode) modelTree.getRoot();

        // visit methods and append to root node
        new MethodTreeVisitor().visit(model.getUnit(), root);

        // tell the view to use the tree model
        view.getMethodTree().setModel(modelTree);

        // make top-level parent nodes' children visible
        expandTopLevelNodes(view.getMethodTree());
    }

    /**
     * Expand top-level tree nodes
     *
     * @param tree tree being expanded
     */
    private void expandTopLevelNodes(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++)
            tree.expandRow(i);
    }

    /**
     * Toggle visibility of progress bar
     *
     * @param visible whether progress dialog should be shown
     */
    private void showProgressDialog(boolean visible) {
        view.getProgressDialog().setVisible(visible);
    }

    /**
     * Read working file from model into source code text-area
     */
    private void readFile() {
        try {
            view.getTextArea().read(new FileReader(model.getWorkingFile()), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: implement real exception here, demonstrate in harness for test report
    private synchronized boolean initCompilationUnit() {
        try {
            model.setUnit(JavaParser.parse(model.getWorkingFile()));
        } catch (FileNotFoundException e) {
            JFrame parent = (JFrame) SwingUtilities.getRoot(view);
            JOptionPane.showMessageDialog(parent, "Problem parsing file!", "Parser error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (ParseProblemException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Callback for method-tree selection event,
     * allows for method-tree to be a navigational component.
     *
     * @param e event
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        // get the selected node
        final Object node =
                view.getMethodTree().getLastSelectedPathComponent();

        // return if null
        if (node == null) return;


        // if node represents a method, jump to beginning line (could possibly hit annotation)
        if (node instanceof MethodTreeNode) {
            // down-cast node related to selection to method node
            final MethodTreeNode method = (MethodTreeNode) node;

            try {
                // make view jump to method's line
                view.jumpToLine(method.getStartLine() - 1);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }


}
