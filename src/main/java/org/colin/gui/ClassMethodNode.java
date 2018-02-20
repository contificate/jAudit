package org.colin.gui;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseEvent;

public class ClassMethodNode extends DefaultMutableTreeNode implements Comparable<ClassMethodNode> {
    public enum Type {
        CLASS, METHOD
    }

    private Type type;
    private String name;
    private Range range;

    private Node node;

    public ClassMethodNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public ClassMethodNode(Type type, String name, Range range) {
        this.type = type;
        this.name = name;
        this.range = range;
    }

    @Override
    public String toString() {
        return (name + " : " + getStartLine());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public int getStartLine() {
        return range.begin.line;
    }

    public int getEndLine() {
        return range.end.line;
    }
/*
    @Override
    public String getToolTipText(MouseEvent e) {

    }
*/
    @Override
    public int compareTo(ClassMethodNode other) {
        return -1; // TODO: fix
    }

}
