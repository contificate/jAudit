package org.colin.visitors;

import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassNameVisitor extends VoidVisitorAdapter<String> {
    @Override
    public void visit(LocalClassDeclarationStmt classDecl, String name) {
        super.visit(classDecl, name);

        name = classDecl.getClassDeclaration().getNameAsString();
        System.out.println(classDecl.getClassDeclaration().getName());
    }



}
