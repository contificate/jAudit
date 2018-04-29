package org.colin.gui.controllers;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.Problem;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import org.colin.audit.AuditContext;
import org.colin.gui.ClassTreeNode;
import org.colin.gui.ClassTreeRenderer;
import org.colin.gui.MethodTreeNode;
import org.colin.gui.graph.AuditGraph;
import org.colin.gui.models.AuditModel;
import org.colin.gui.models.AuditorModel;
import org.colin.gui.models.ParseProblemModel;
import org.colin.gui.views.AuditView;
import org.colin.gui.views.AuditorView;
import org.colin.gui.views.ParseProblemView;
import org.colin.visitors.MethodTreeVisitor;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

import static org.colin.res.IconLoader.loadIcon;
import static org.colin.res.IconNames.*;

/**
 * Controller used by the {@link AuditView} view.
 */
public class AuditController implements TreeSelectionListener {

    /**
     * Model used for updating the view.
     */
    private AuditModel model;

    /**
     * Auditing view used to invoke controller.
     */
    private AuditView view;

    private boolean parsedFile = false;

    /**
     * Create audit controller to cohere both model and view.
     *
     * @param model model that updates what's shown by {@link AuditView}
     * @param view  view that invokes controller's actions
     */
    public AuditController(AuditModel model, AuditView view) {
        this.model = model;
        this.view = view;

        // initialise auxiliary actions
        initRenderers();
        initAuditMenu();
        initListeners();

        // show progress dialog as audit pane is being initialised
        showProgressDialog(true);

        // read file into text area
        readFile();
    }

    public boolean parse() {
        // parse provided file into searchable AST structure in a thread-safe manner;
        // runs in the background as to not block GUI thread
        ParserWorker worker = new ParserWorker();
        worker.execute();
        try {
            return worker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void initRenderers() {
        view.getMethodTree().setCellRenderer(new ClassTreeRenderer());
    }

    /**
     * Initialise listeners for view to perform event-based callbacks in the controller
     */
    private void initListeners() {
        view.setTreeListener(this);
    }

    /**
     * Create compilation unit (parse file) in background
     * as to avoid blocking the GUI thread.
     */
    private class ParserWorker extends SwingWorker<Boolean, Boolean> {

        @Override
        protected Boolean doInBackground() throws ParseProblemException {
            // attempt to parse file
            parsedFile = initCompilationUnit();

            // if parsing is successful, run method visitor to initialise method-tree
            if (parsedFile) {
                initTree();
            }

            // if parsing failed without throwing ParseProblemException, return success value anyway
            return parsedFile;
        }

        /**
         * Parsing complete callback, closes progress dialog.
         */
        @Override
        protected void done() {
            // queue progress-dialog closure in AWT event queue
            SwingUtilities.invokeLater(() -> showProgressDialog(false));

            // state
            try {
                parsedFile = get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isParsedFile() {
        return parsedFile;
    }

    /**
     * Action performed when an audit is intended to be created.
     */
    private class ViewDepthAction extends AbstractAction {

        protected AuditContext contextTrace;

        public ViewDepthAction() {
            super(view.getLocalised("view_depth"), loadIcon(AST_DEPTH_ICON));
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

                // single line selections will likely be snapped selections so we
                // reduce the selection to get more accurate positions
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

                // continually pop and traverse and push all children of internal nodes
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
            putValue("SmallIcon", loadIcon(TREE_ICON));
            putValue("MnemonicKey", KeyEvent.VK_A);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            super.actionPerformed(actionEvent);

            SwingUtilities.invokeLater(() -> {
                AuditorModel model = new AuditorModel(contextTrace);
                AuditorView view = new AuditorView(null);
                AuditorController controller = new AuditorController(model, view);
                // TODO: get result from this shit, and then add to AuditModel from here and then create document builder stuff
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

    // TODO: implement real exception here, demonstrate in harness for test report, use view's getLocalised
    private synchronized boolean initCompilationUnit() {
        try {
            // initialise model with compilation unit built from parsing the file
            model.setUnit(JavaParser.parse(model.getWorkingFile()));
        } catch (FileNotFoundException e) {
            return false;
        } catch (ParseProblemException ex) {

            final JFrame parent = (JFrame) SwingUtilities.getRoot(view);
            final String message = view.getLocalised("parser_error"), desc = view.getLocalised("error_prompt");
            int option = JOptionPane.showConfirmDialog(parent, desc, message, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, loadIcon(ERROR_ICON));

            if(option == JOptionPane.YES_OPTION) {
                ArrayList<Problem> problems = new ArrayList<>(ex.getProblems());
                ParseProblemModel model = new ParseProblemModel(problems, this.model.getWorkingFile());
                ParseProblemView view = new ParseProblemView(parent);
                new ParseProblemController(model, view);

                view.setVisible(true);
            }

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
