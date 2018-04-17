package org.colin.gui.controllers;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import org.colin.gui.ClassTreeNode;
import org.colin.gui.ClassTreeRenderer;
import org.colin.gui.MethodTreeNode;
import org.colin.gui.graph.DrawableNode;
import org.colin.gui.graph.GraphDrawable;
import org.colin.gui.graph.GraphView;
import org.colin.gui.models.AuditModel;
import org.colin.gui.views.AuditView;
import org.colin.res.IconLoader;
import org.colin.visitors.MethodTreeVisitor;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class AuditController implements TreeSelectionListener {

    private AuditModel model;
    private AuditView view;

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
            // queue progress-dialog closure in AWT event queuehttp://10.163.2.32
            SwingUtilities.invokeLater(() -> {
                showProgressDialog(false);
            });
        }
    }

    private class AuditAction extends AbstractAction {
        public AuditAction() {
            super("Create audit", IconLoader.loadIcon("annotation.png"));
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            final RSyntaxTextArea textArea = view.getTextArea();

            final int beginOffset = textArea.getSelectionStart(), endOffset = textArea.getSelectionEnd();
            try {
                // get line offsets
                final int beginLine = textArea.getLineOfOffset(beginOffset),
                        endLine = textArea.getLineOfOffset(endOffset);

                // work out selection columns
                int startLineOffset = textArea.getLineStartOffset(beginLine);
                int beginColumn = (beginOffset - startLineOffset),
                        endColumn = (endOffset - startLineOffset);

                // single line selections will likely be snapped selections so we reduce the selection to get more accurate positions
                if(beginLine == endLine) {
                    beginColumn++;
                    endColumn--;
                }

                // create selection range
                final Range selectionRange = Range.range((beginLine + 1), beginColumn, (endLine + 1), endColumn);

                // selectively depth-first search AST
                final Node rootNode = model.getUnit().getParentNodeForChildren();

                // TODO: delegate to private member for context for cloneable back-propagation
                ArrayList<GraphDrawable> trace = new ArrayList<>();

                // AST traversal stack
                Stack<Node> toVisit = new Stack<>();
                toVisit.push(rootNode);

                // continually pop and traverse and push all childern of interior nodes
                while(!(toVisit.empty())) {
                    final Node node = toVisit.pop();
                    final Range nodeRange = node.getRange().orElse(Range.range(0, 0, 0, 0));

                    if(nodeRange.contains(selectionRange)) {
                        // TODO: remove debug
                        System.out.println(node.getClass().toString());

                        trace.add(new DrawableNode(node));

                        for(final Node child : node.getChildNodes()) {
                            toVisit.push(child);
                        }
                    }

                }

                GraphView graphView = view.getGraphView();
                graphView.setVertices(trace);
                SwingUtilities.invokeLater(() -> graphView.repaint());

            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    private void initAuditMenu() {
        JPopupMenu auditMenu = new JPopupMenu();
        auditMenu.add(new AuditAction());
        auditMenu.addSeparator();
        view.getTextArea().setPopupMenu(auditMenu);
        view.getMethodTree().setComponentPopupMenu(auditMenu);
    }

    /**
     * Initialise method-tree
     */
    private void initTree() {
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
     * @param tree tree being expanded
     */
    private void expandTopLevelNodes(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++)
            tree.expandRow(i);
    }

    private void showProgressDialog(boolean visible) {
        view.getProgressDialog().setVisible(visible);
    }

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
            final MethodTreeNode method = (MethodTreeNode) node;

            try {
                view.jumpToLine(method.getStartLine() - 1);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

    }


}
