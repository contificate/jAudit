package org.colin.gui.models;

import com.github.javaparser.ast.Node;
import org.colin.audit.AuditContext;
import org.colin.gui.ASTListElement;

import javax.swing.*;

public class AuditorModel {

    private DefaultListModel<ASTListElement> listModel;
    private AuditContext context;

    public AuditorModel(AuditContext context) {
        this.context = context;
        initModel();
    }

    private void initModel() {
        listModel = new DefaultListModel<>();

        for(Node node : context)
            listModel.addElement(new ASTListElement(node));
    }

    public DefaultListModel<ASTListElement> getListModel() {
        return listModel;
    }
}
