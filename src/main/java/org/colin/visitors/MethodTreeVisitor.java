package org.colin.visitors;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.colin.gui.ClassMethodNode;
import org.colin.gui.ClassTreeNode;
import org.colin.gui.MethodTreeNode;

import javax.swing.*;
import java.util.Optional;

public class MethodTreeVisitor extends VoidVisitorAdapter<ClassTreeNode> {

    @Override
    public void visit(MethodDeclaration methodDecl, ClassTreeNode root) {
        super.visit(methodDecl, root);
        root.add(new MethodTreeNode(methodDecl));
    }

}
