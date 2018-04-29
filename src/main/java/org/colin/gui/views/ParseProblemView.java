package org.colin.gui.views;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import org.colin.main.Main;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * View used to display any errors encountered when parser attempts to parse Java source file.
 */
public class ParseProblemView extends JDialog {
    /**
     * Source view used for displaying content of file that couldn't be parsed successfully.
     */
    private RSyntaxTextArea textArea;

    /**
     * Table used for displaying problems related to content shown in {@link ParseProblemView#textArea}
     */
    private JTable table;

    /**
     * Highlighter for highlighting errors within source code shown in {@link ParseProblemView#textArea}
     */
    private Highlighter highlighter;

    /**
     *
     */
    private final Highlighter.HighlightPainter painter =
            new DefaultHighlighter.DefaultHighlightPainter(Color.pink);

    private ResourceBundle rb = ResourceBundle.getBundle(getClass().getSimpleName(), Main.locale);

    public ParseProblemView(JFrame parent) {
        super(parent, true);
        setTitle(rb.getString("title"));
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
        JPanel layout = new JPanel(new BorderLayout());

        // create syntax highlighted source view
        textArea = new RSyntaxTextArea();
        textArea.setAntiAliasingEnabled(true);
        textArea.setHighlightCurrentLine(false);
        textArea.setEditable(false);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

        // set highlighter from source view's highlighter
        highlighter = textArea.getHighlighter();


        table = new JTable();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new RTextScrollPane(textArea), new JScrollPane(table));
        splitPane.setDividerLocation(180);
        splitPane.setResizeWeight(0.4);

        layout.add(splitPane);

        add(layout);
    }

    public String getLocalised(@Nonnull String key) {
        return rb.getString(key);
    }

    /**
     * Highlight textual range using {@link ParseProblemView#highlighter}
     * @param range range to be highlighted
     */
    public void highlightRange(Range range) {
        try {
            final Position startPos = range.begin, endPos = range.end;
            final int start = (textArea.getLineStartOffset(startPos.line - 1) + startPos.column), end = (textArea.getLineEndOffset(endPos.line - 1) + endPos.column);
            highlighter.addHighlight(start, end, painter);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public JTable getTable() {
        return table;
    }

    public RSyntaxTextArea getTextArea() {
        return textArea;
    }
}
