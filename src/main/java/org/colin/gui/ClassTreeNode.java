package org.colin.gui;

import org.colin.res.IconLoader;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import static org.colin.res.IconNames.CLASS_ICON;

/**
 * Tree node for classes<br>
 */
public class ClassTreeNode extends DefaultMutableTreeNode implements TreeDrawable {

    private String name;

    public ClassTreeNode(String name) {
        this.name = name;
    }

    @Override
    public String getText() {
        return ("<html><h4>" + name + "</h4></html>");
    }

    @Override
    public ImageIcon getIcon() {
        return IconLoader.loadIcon(CLASS_ICON);
    }


}
