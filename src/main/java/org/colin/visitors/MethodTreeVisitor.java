package org.colin.visitors;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.colin.gui.ClassMethodNode;

import javax.swing.*;
import java.util.Optional;

public class MethodTreeVisitor extends VoidVisitorAdapter<ClassMethodNode> {

    @Override
    public void visit(MethodDeclaration methodDecl, ClassMethodNode root) {
        super.visit(methodDecl, root);
        final String name = methodDecl.getNameAsString();

        final Range range = methodDecl.getRange().orElse(Range.range(0, 0, 0, 0));

        root.add(new ClassMethodNode(ClassMethodNode.Type.METHOD, name, range));
    }

}
