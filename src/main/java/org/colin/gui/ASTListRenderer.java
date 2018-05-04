package org.colin.gui;

import org.colin.res.IconLoader;

import javax.swing.*;
import java.awt.*;

import static org.colin.res.IconNames.*;


/**
 * Renderer for AST context list.
 */
public class ASTListRenderer extends JLabel implements ListCellRenderer<ASTListElement> {
    /**
     * Render an AST node for the AST context list view.
     *
     * @param list     list of AST elements
     * @param node     AST node being rendereed
     * @param i        index
     * @param selected if node is selected
     * @param focused  if node is focused
     * @return component to render on behalf of node (this, a JLabel)
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends ASTListElement> list, ASTListElement node,
                                                  int i, boolean selected, boolean focused) {
        // set icon dependent on whether it's selected
        setIcon(selected ? IconLoader.loadIcon(TREE_SELECTED_ICON) : IconLoader.loadIcon(TREE_ICON));

        // set HTML-formatted label
        setText("<html><b>" + node.getName() + "</b> " + node.getDescription() + "</html>");

        // return this to render on behalf of the ASTListElement
        return this;
    }
}
