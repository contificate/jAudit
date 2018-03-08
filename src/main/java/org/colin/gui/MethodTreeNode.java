package org.colin.gui;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.colin.res.IconLoader;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import static org.colin.res.IconNames.METHOD_ICON;

public class MethodTreeNode extends DefaultMutableTreeNode implements TreeDrawable {
    private MethodDeclaration node;

    public MethodTreeNode(MethodDeclaration node) {
        this.node = node;
    }

    public MethodDeclaration getNode() {
        return node;
    }

    public String getName() {
        return node.getNameAsString();
    }

    public int getStartLine() {
        return node.getRange().orElse(Range.range(0, 0, 0, 0)).begin.line;
    }

    public int getEndLine() {
        return node.getRange().orElse(Range.range(0, 0, 0, 0)).end.line;
    }

    @Override
    public String getText() {
        return ("<html><b>" + getName() + "</b> : <font color=\"#ccc\">" +  getStartLine()  + String.valueOf("\u2192") + getEndLine() + "</font></html>");
    }

    @Override
    public ImageIcon getIcon() {
        return IconLoader.loadIcon(METHOD_ICON);
    }
}
