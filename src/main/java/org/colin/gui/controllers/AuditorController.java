package org.colin.gui.controllers;

import com.github.javaparser.ast.Node;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.colin.audit.AuditContext;
import org.colin.audit.AuditSerializer;
import org.colin.gui.ASTListElement;
import org.colin.gui.models.AuditorModel;
import org.colin.gui.views.AuditorView;
import org.colin.gui.views.RemoteLoaderView;

import javax.swing.*;

/**
 * Controller used by the {@link AuditorView} view.
 */
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

        view.setOnCancel(e -> view.dispose());
        view.setOnCreate(e -> {
            // get selected indices for sublisting
            int[] indices = view.getList().getSelectedIndices();
            int first = indices[0], last = indices[(indices.length - 1)];
            model.reduceContext(first, last + 1);

            AuditSerializer serializer = new AuditSerializer();
            Gson gson = new GsonBuilder().registerTypeAdapter(AuditContext.class, new AuditSerializer()).setPrettyPrinting().create();
            System.out.println(gson.toJson(model.getContext()));
        });
    }

}
