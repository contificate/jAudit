package org.colin.gui;

import sun.reflect.generics.tree.Tree;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Custom renderer for class-method tree
 */
public class ClassTreeRenderer extends DefaultTreeCellRenderer {
    /**
     * Set value at tree cell with custom rendered node
     * @param tree tree
     * @param value node from tree
     * @param selected is node selected?
     * @param expanded is node an expanded parent?
     * @param leaf is node a leaf?
     * @param row row of node
     * @param focused true if focused
     * @return tree node component
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean focused) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, focused);

        // if the node is drawable, draw it using its provided label and icon
        if(value instanceof TreeDrawable) {
            TreeDrawable node = (TreeDrawable) value;
            setText(node.getText());
            setIcon(node.getIcon());
        }

        // return this renderer for relevant JTree to use for value
        return this;
    }

}
