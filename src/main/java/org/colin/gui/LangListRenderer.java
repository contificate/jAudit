package org.colin.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Renderer used by {@link org.colin.gui.views.LangSelectionView}
 */
public class LangListRenderer extends JLabel implements ListCellRenderer<LanguageOption> {
    /**
     * Get the component used to render the list item.
     * In this case, the component is the super class, {@link JLabel}.
     *
     * @param list           list of options
     * @param languageOption the language option being rendered
     * @param index          the index of the language option being rendered
     * @param selected       true if the item is selected
     * @param focused        true if the item is focused
     * @return component to be rendered on behalf of the {@link LanguageOption} list item
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends LanguageOption> list, LanguageOption languageOption, int index, boolean selected, boolean focused) {
        // set the label's text to the language string
        setText(languageOption.getLanguage());

        // set the label's icon to the flag
        setIcon(languageOption.getFlag());

        // add an empty border for aesthetics
        setBorder(new EmptyBorder(5, 5, 5, 5));

        // return this, e.g. the label being rendered
        return this;
    }
}
