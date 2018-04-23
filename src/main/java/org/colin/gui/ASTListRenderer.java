package org.colin.gui;

import org.colin.res.IconLoader;

import javax.swing.*;
import java.awt.*;

import static org.colin.res.IconNames.*;


public class ASTListRenderer extends JLabel implements ListCellRenderer<ASTListElement> {
    @Override
    public Component getListCellRendererComponent(JList<? extends ASTListElement> jList, ASTListElement astListElement, int i, boolean selected, boolean focused) {
        setIcon(selected ? IconLoader.loadIcon(TREE_SELECTED_ICON) : IconLoader.loadIcon(TREE_ICON));
        setText("<html><b>" + astListElement.getName() + "</b> " + astListElement.getDescription() + "</html>");
        return this;
    }
}
