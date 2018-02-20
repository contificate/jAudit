package org.colin.gui;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.colin.res.IconLoader;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.lang.reflect.Method;

import static org.colin.gui.ClassMethodNode.Type.CLASS;
import static org.colin.gui.ClassMethodNode.Type.METHOD;
import static org.colin.res.IconNames.*;

public class ClassTreeRenderer extends DefaultTreeCellRenderer {
/*
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean focused) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, focused);

        ClassMethodNode node = (ClassMethodNode) value;

        setToolTipText("juden");

        switch (node.getType()) {
            case CLASS: {
                setIcon(IconLoader.loadIcon(CLASS_ICON));
                break;
            }

            case METHOD: {
                setIcon(IconLoader.loadIcon(METHOD_ICON));
                setText("<html><b>" + node.getName() + "</b> : <font color=\"#ccc\">" +  node.getStartLine()  + String.valueOf("\u2192") + node.getEndLine() + "</font></html>");
                break;
            }
        }

        return this;
    }*/

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean focused) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, focused);

        ClassMethodNode node = (ClassMethodNode) value;

        switch (node.getType()) {
            case CLASS: {
                setIcon(IconLoader.loadIcon(CLASS_ICON));
                break;
            }

            case METHOD: {
                setIcon(IconLoader.loadIcon(METHOD_ICON));
                setText("<html><b>" + node.getName() + "</b> : <font color=\"#ccc\">" +  node.getStartLine()  + String.valueOf("\u2192") + node.getEndLine() + "</font></html>");
                break;
            }
        }

        return this;
    }

}
