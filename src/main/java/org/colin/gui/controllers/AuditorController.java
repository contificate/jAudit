package org.colin.gui.controllers;

import org.colin.gui.ASTListElement;
import org.colin.gui.models.AuditorModel;
import org.colin.gui.views.AuditorView;

import javax.swing.*;

public class AuditorController {

    private AuditorModel model;
    private AuditorView view;

    public AuditorController(AuditorModel model, AuditorView view) {
        this.model = model;
        this.view = view;

        initController();
    }

    private void initController() {
        final JList<ASTListElement> listView = view.getList();
        listView.setModel(model.getListModel());
    }

}
