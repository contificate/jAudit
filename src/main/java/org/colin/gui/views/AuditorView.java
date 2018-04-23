package org.colin.gui.views;

import com.alee.laf.button.WebButton;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;
import org.colin.gui.ASTListElement;
import org.colin.gui.ASTListRenderer;
import org.colin.main.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ResourceBundle;

public class AuditorView extends JDialog {

    private JPanel layout;
    private JList<ASTListElement> list;
    private WebTextArea commentArea;

    private ResourceBundle rb = ResourceBundle.getBundle(getClass().getSimpleName(), Main.locale);

    public AuditorView(JFrame parent) {
        super(parent, true);
        setTitle(rb.getString("dialog_title"));
        setSize(380, 500);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        layout = new JPanel(new BorderLayout());
        layout.setBorder(new EmptyBorder(10, 10, 20, 10));

        list = new JList<>();
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setCellRenderer(new ASTListRenderer());

        commentArea = new WebTextArea();
        commentArea.setInputPrompt(rb.getString("input_prompt"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(list), new WebScrollPane(commentArea));
        splitPane.setDividerLocation(0.2);
        layout.add(splitPane, BorderLayout.CENTER);

        JPanel buttonBox = new JPanel();
        buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.LINE_AXIS));
        buttonBox.add(Box.createHorizontalGlue()); // add growing spacer to align all children to the right
        buttonBox.add(new WebButton(rb.getString("cancel")));
        buttonBox.add(Box.createRigidArea(new Dimension(10, 0))); // add spacer between buttons
        buttonBox.add(new WebButton(rb.getString("create")));
        layout.add(buttonBox, BorderLayout.PAGE_END);

        add(layout);
    }

    public JList<ASTListElement> getList() {
        return list;
    }

}
