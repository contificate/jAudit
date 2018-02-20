package org.colin.visitors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.colin.gui.ClassMethodNode;

public class ClassNameVisitor extends VoidVisitorAdapter<ClassMethodNode> {
    @Override
    public void visit(ClassOrInterfaceType classDecl, ClassMethodNode name) {
        super.visit(classDecl, name);
        System.out.println("Debug");
    }

}
