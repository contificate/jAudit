package org.colin.gui;

import com.github.javaparser.ast.Node;

public class ASTListElement {

    private Node node;

    private String name;
    private String description;

    public ASTListElement(Node node) {
        this.node = node;

        name = node.getClass().getSimpleName();
        description = this.node.toString();
        if(description.length() >= 30) {
            description = (description.substring(0, 30) + "...");
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
