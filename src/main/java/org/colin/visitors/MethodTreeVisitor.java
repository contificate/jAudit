package org.colin.visitors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.colin.gui.ClassTreeNode;
import org.colin.gui.MethodTreeNode;

/**
 * Method-visitor designed for appendage to method-tree
 */
public class MethodTreeVisitor extends VoidVisitorAdapter<ClassTreeNode> {

    /**
     * Visit AST method
     * @param methodDecl method declaration node
     * @param root tree node being appended to (where the methods are leaves)
     */
    @Override
    public void visit(MethodDeclaration methodDecl, ClassTreeNode root) {
        super.visit(methodDecl, root); // ensure relevant siblings are traversed by adapter (parent)

        // add method to root node
        root.add(new MethodTreeNode(methodDecl));
    }

}
