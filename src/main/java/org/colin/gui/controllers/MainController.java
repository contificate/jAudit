package org.colin.gui.controllers;

import org.colin.actions.FileIntent;
import org.colin.actions.FileReceiver;
import org.colin.gui.models.AuditModel;
import org.colin.gui.views.AuditView;
import org.colin.gui.views.MainView;

import java.io.File;

/**
 *
 */
public class MainController implements FileReceiver {
    /**
     * View that the controller is responsible for updating
     */
    private MainView view;

    /**
     * Initialise primary view
     *
     * @param view injected view
     */
    public MainController(MainView view) {
        this.view = view;
        this.view.setFileReceiver(this);
    }

    /**
     * Implements {@link FileReceiver} for {@link MainView}
     * Currently only used for opening Java source files.
     *
     * @param file   file being received
     * @param intent intention with file (open, etc.)
     */
    @Override
    public void receiveFile(File file, FileIntent intent) {
        switch (intent) {
            case OPEN: {
                // initialise audit model with file
                AuditModel model = new AuditModel(file);

                // inject model into view
                AuditView view = new AuditView(model);

                // initialise controller for model and view
                AuditController controller = new AuditController(model, view);

                // create tab for audit pane and show it
                this.view.addAuditTab(file.getName(), view);
                this.view.focusLastTab();
                break;
            }
        }
    }
}
