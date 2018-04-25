package org.colin.gui.views;

import org.colin.gui.LanguageOption;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * View used to allow user to select language upon startup
 */
public class LangSelectionView extends JDialog {

    /**
     * Combo-box component used for displaying and selecting supported languages
     */
    private JComboBox<LanguageOption> optionsBox = new JComboBox<>();

    /**
     * Initialise dialog properties
     *
     * @param parent parent JFrame (often null if used on startup)
     */
    public LangSelectionView(JFrame parent) {
        super(parent, true);
        setResizable(false);
        setSize(200, 130);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        initComponents();
    }

    /**
     * Initialise layout and its components
     */
    private void initComponents() {
        // create simple vertical grid
        JPanel layout = new JPanel(new GridLayout(2, 0));
        // add an empty border (for aesthetic purposes)
        layout.setBorder(new EmptyBorder(10, 10, 20, 10));

        // add the components
        layout.add(new JLabel("Select language:"));
        layout.add(optionsBox);

        // set layout as root component
        add(layout);
    }

    /**
     * Get the combo-box used for displaying and selecting supported languages.
     *
     * @return combo-box containing language options (see {@link LanguageOption})
     */
    public JComboBox<LanguageOption> getOptionsBox() {
        return optionsBox;
    }
}
