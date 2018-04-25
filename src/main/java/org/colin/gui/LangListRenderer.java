package org.colin.gui;

import com.alee.utils.ColorUtils;
import org.colin.util.ColourUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LangListRenderer extends JLabel implements ListCellRenderer<LanguageOption> {

    @Override
    public Component getListCellRendererComponent(JList<? extends LanguageOption> list, LanguageOption languageOption, int i, boolean selected, boolean focused) {
        setText(languageOption.getLanguage());
        setIcon(languageOption.getFlag());
        setBorder(new EmptyBorder(5, 5, 5, 5));
        if(selected) {
            setBackground(ColourUtil.fromHex("#00ff00"));
        }
        return this;
    }
}
