package org.colin.gui.views;

import com.alee.laf.button.WebButton;
import com.alee.laf.text.WebTextField;
import org.colin.main.Main;
import org.colin.util.ColourUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import static org.colin.res.IconLoader.loadIcon;
import static org.colin.res.IconNames.GITHUB_ICON;

public class RemoteLoaderView extends JDialog {

    private WebTextField textField;
    private WebButton cancelBtn;
    private WebButton loadBtn;

    private final Color validColour = ColourUtil.fromHex("#4dff4f");
    private final Color invalidColour = ColourUtil.fromHex("#ff6363");

    private Border cachedBorder;

    private ResourceBundle rb = ResourceBundle.getBundle(getClass().getSimpleName(), Main.locale);

    public RemoteLoaderView(JFrame parent) {
        super(parent, true);
        setTitle(rb.getString("title"));
        setSize(300, 120);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
        JPanel layout = new JPanel(new GridLayout(3, 0));
        layout.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        layout.add(new JLabel("GitHub URL:", loadIcon(GITHUB_ICON), SwingConstants.LEFT));

        textField = new WebTextField();
        cachedBorder = textField.getBorder();
        layout.add(textField);

        JPanel buttonBox = new JPanel();
        buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.LINE_AXIS));
        buttonBox.add(Box.createHorizontalGlue());
        cancelBtn = new WebButton(rb.getString("cancel"));
        buttonBox.add(cancelBtn);
        buttonBox.add(Box.createRigidArea(new Dimension(10, 0)));
        loadBtn = new WebButton(rb.getString("load"));
        buttonBox.add(loadBtn);

        layout.add(buttonBox);

        setValidInput(false);
        add(layout);
    }

    public void setOnCancel(ActionListener listener) {
        cancelBtn.addActionListener(listener);
    }

    public void setOnLoad(ActionListener listener) {
        loadBtn.addActionListener(listener);
    }

    public void setValidInput(boolean valid) {
        if(textField.getText().isEmpty())
            textField.setBorder(cachedBorder);
        // change colour of text-field's border to signify validity
        final Border colouredBorder = BorderFactory.createLineBorder((valid ? validColour : invalidColour), 2);
        final Border spacedBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        textField.setBorder(BorderFactory.createCompoundBorder(colouredBorder, spacedBorder));

        // toggle access to button to disable user from submitting invalid input
        loadBtn.setEnabled(valid);
    }

    public WebTextField getTextField() {
        return textField;
    }

}
