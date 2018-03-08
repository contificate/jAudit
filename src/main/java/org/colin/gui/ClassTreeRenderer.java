package org.colin.gui;

import sun.reflect.generics.tree.Tree;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class ClassTreeRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean focused) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, focused);

        if(value instanceof TreeDrawable) {
            TreeDrawable node = (TreeDrawable) value;
            setText(node.getText());
            setIcon(node.getIcon());
        }

        return this;
    }

}
