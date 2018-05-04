package org.colin.gui;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.colin.res.IconLoader;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import static org.colin.res.IconNames.METHOD_ICON;

/**
 * Method tree node used for representing method leaves in a tree.
 * Class is essentially a wrapper-adapter where the adaptee is the node being represented.
 * The adaptive interface is {@link TreeDrawable}.
 */
public class MethodTreeNode extends DefaultMutableTreeNode implements TreeDrawable {
    /**
     * Wrapped adaptee node
     */
    private MethodDeclaration node;

    /**
     * Construct a method tree node
     *
     * @param node node to wrap
     */
    public MethodTreeNode(MethodDeclaration node) {
        this.node = node;
    }

    /**
     * Get the node being wrapped
     *
     * @return method declaration node
     */
    public MethodDeclaration getNode() {
        return node;
    }

    /**
     * Get name of method
     *
     * @return method's simple name
     */
    public String getName() {
        return node.getNameAsString();
    }

    /**
     * Get the start line of the wrapped method declaration
     *
     * @return start line of method
     */
    public int getStartLine() {
        return node.getRange().orElse(Range.range(0, 0, 0, 0)).begin.line;
    }

    /**
     * Get the end line of the wrapped method declaration
     *
     * @return end line of method
     */
    public int getEndLine() {
        return node.getRange().orElse(Range.range(0, 0, 0, 0)).end.line;
    }

    /**
     * Get text to be displayed in tree
     *
     * @return text to be displayed
     */
    @Override
    public String getText() {
        return ("<html><b>" + getName() + "</b> : <font color=\"#ccc\">" + getStartLine() + String.valueOf("\u2192") + getEndLine() + "</font></html>");
    }

    /**
     * Get icon to be displayed in tree
     *
     * @return icon to be displayed
     */
    @Override
    public ImageIcon getIcon() {
        return IconLoader.loadIcon(METHOD_ICON);
    }
}
