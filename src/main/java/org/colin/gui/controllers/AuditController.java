package org.colin.gui.controllers;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.Problem;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.Pair;
import org.colin.audit.AuditContext;
import org.colin.db.DBConnection;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    /**
     * State of "parsedness", used for determining logic.
     */
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

        // check database and compare model's checksum
        checkDatabase();

        // if aborted during database check, abort
        if (model.hasError()) {
            return;
        }

        // initialise auxiliary actions
        initRenderers();
        initAuditMenu();
        initListeners();

        // show progress dialog as audit pane is being initialised
        showProgressDialog(true);

        // read file into text area
        readFile();

        // read existing audits
        readExistingAudits();
    }

    private void readExistingAudits() {
        // get singleton database connection
        DBConnection db = DBConnection.getInstance();

        // if connected, fetch audits
        if (db.isConnected()) {
            try {
                // create a prepared statement for fetching audits from database
                PreparedStatement stmt = db.prepare("SELECT * FROM `audits` WHERE file_id=?");
                stmt.setInt(1, model.getFileId());
                ResultSet rs = stmt.executeQuery();

                // for each found audit, create a bookmark at the beginning line w/ comment as tooltip
                while (rs.next()) {
                    // begin line, comment
                    addBookmark(rs.getInt(3), rs.getString(6));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkDatabase() {
        // get DB singleton
        DBConnection db = DBConnection.getInstance();

        // if connected, try query for file
        if (db.isConnected()) {
            try {
                // get all columns for file if exist
                PreparedStatement stmt = db.prepare("SELECT * FROM `files` WHERE path=? LIMIT 1");
                stmt.setString(1, model.getWorkingFile().getPath());
                ResultSet rs = stmt.executeQuery();

                // if file already referenced in database
                if (rs.next()) {
                    // get stored checksum
                    long checksum = rs.getLong(3);

                    // compare stored checksum to model's just-calculated checksum
                    if (checksum != model.getFileChecksum()) {
                        // ask user if they want to continue
                        int option = JOptionPane.showConfirmDialog(null, view.getLocalised("checksum_error"),
                                view.getLocalised("error"),
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.ERROR_MESSAGE,
                                loadIcon(ERROR_ICON));

                        // set the error flag so file doesn't get loaded
                        model.setHasError(option == JOptionPane.NO_OPTION);
                    }

                    // give the model the database file id
                    model.setFileId(rs.getInt(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
                view.setVisible(true);

                if (writeAuditToDatabase(model)) {

                    System.out.println("wtf");

                    JOptionPane.showMessageDialog(null, "Successfully created audit", "Success",
                            JOptionPane.INFORMATION_MESSAGE, loadIcon(JAUDIT_ICON));

                    final Pair<Integer, Integer> range = model.getContext().getLineRange();;
                    addBookmark(range.a, model.getComment());
                }
            });

        }
    }

    private void addBookmark(final int line, final String comment) {
        try {
            view.getGutter().addLineTrackingIcon((line - 1), loadIcon(ANNOTATION_ICON), comment);
        } catch (BadLocationException e) {
            // ignore
        }
    }

    private boolean writeAuditToDatabase(AuditorModel model) {
        DBConnection db = DBConnection.getInstance();

        if (db.isConnected()) {
            try {
                PreparedStatement stmt = db.prepare("INSERT INTO `audits` (file_id, begin, end, context, comment) " +
                        "VALUES (?, ?, ?, ?, ?)");

                final Pair<Integer, Integer> lineRange = model.getContext().getLineRange();
                stmt.setInt(1, this.model.getFileId());
                stmt.setInt(2, lineRange.a);
                stmt.setInt(3, lineRange.b);
                stmt.setString(4, model.getAuditJson());
                stmt.setString(5, model.getComment());

                stmt.execute();

                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
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

    /**
     * Initialise compilation unit by parsing file
     * @return whether file parsing was successful
     */
    private synchronized boolean initCompilationUnit() {
        try {
            // initialise model with compilation unit built from parsing the file
            model.setUnit(JavaParser.parse(model.getWorkingFile()));
        } catch (FileNotFoundException e) {
            return false;
        } catch (ParseProblemException ex) {
            // get root frame
            final JFrame parent = (JFrame) SwingUtilities.getRoot(view);

            // get localised error message and description
            final String message = view.getLocalised("parser_error"), desc = view.getLocalised("error_prompt");

            // ask the user if they'd like to see the parser problems
            int option = JOptionPane.showConfirmDialog(parent, desc, message, JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    loadIcon(ERROR_ICON));

            // if they would like to see problems, show problem view
            if (option == JOptionPane.YES_OPTION) {
                // get problem(s) from exception
                ArrayList<Problem> problems = new ArrayList<>(ex.getProblems());

                // construct problem model with problems and relevant file
                ParseProblemModel model = new ParseProblemModel(problems, this.model.getWorkingFile());

                // show view
                ParseProblemView view = new ParseProblemView(parent);
                new ParseProblemController(model, view);
                view.setVisible(true);
            }

            // return failure
            return false;
        }

        // if reached here, success
        return true;
    }

    /**
     * Callback for method-tree selection event,
     * allows for method-tree to be a navigational component.
     *
     * @param e selection event
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
