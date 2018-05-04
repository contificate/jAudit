package org.colin.gui.models;

import com.github.javaparser.ast.Node;
import org.colin.audit.AuditContext;
import org.colin.gui.ASTListElement;

import javax.swing.*;

public class AuditorModel {

    private DefaultListModel<ASTListElement> listModel;
    private AuditContext context;
    private String auditJson;
    private String comment;

    public AuditorModel(AuditContext context) {
        this.context = context;
        initModel();
    }

    private void initModel() {
        listModel = new DefaultListModel<>();

        for(Node node : context)
            listModel.addElement(new ASTListElement(node));
    }

    public void setAuditJson(String auditJson) {
        this.auditJson = auditJson;
    }

    public String getAuditJson() {
        return auditJson;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void reduceContext(int first, int last) {
        context.sublist(first, last);
    }

    public AuditContext getContext() {
        return context;
    }

    public DefaultListModel<ASTListElement> getListModel() {
        return listModel;
    }
}
