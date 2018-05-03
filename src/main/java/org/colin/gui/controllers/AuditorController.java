package org.colin.gui.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.colin.audit.AuditContext;
import org.colin.audit.AuditSerializer;
import org.colin.gui.ASTListElement;
import org.colin.gui.models.AuditorModel;
import org.colin.gui.views.AuditorView;

import javax.swing.*;

/**
 * Controller used by the {@link AuditorView} view.
 */
public class AuditorController {
    /**
     * Auditor model used to store context etc.
     */
    private AuditorModel model;

    /**
     * View used to invoke this controller.
     */
    private AuditorView view;

    /**
     * Initialise controller with model and view
     *
     * @param model model that updates what's shown by {@link AuditorView}
     * @param view  view that invokes controller's actions
     */
    public AuditorController(AuditorModel model, AuditorView view) {
        this.model = model;
        this.view = view;

        // initialise controller
        initController();
    }

    private void initController() {
        // update view with list model from the auditor model
        final JList<ASTListElement> listView = view.getList();
        listView.setModel(model.getListModel());

        // on cancel, simply dispose of view
        view.setOnCancel(e -> view.dispose());

        // on create, transpose selected context to JSON and update model
        view.setOnCreate(e -> {
            // get selected indices for sub-listing
            int[] indices = view.getList().getSelectedIndices();
            int first = indices[0], last = indices[(indices.length - 1)];

            // reduce context to inner sublist
            model.reduceContext(first, last + 1);

            // register type adapter for transposing AuditContext to JSON
            Gson gson = new GsonBuilder().registerTypeAdapter(AuditContext.class, new AuditSerializer()).create();

            // set the context JSON
            model.setAuditJson(gson.toJson(model.getContext()));

            // set the comment
            model.setComment(view.getComment());

            // return to caller's context by disposing of view
            view.dispose();
        });
    }

}
