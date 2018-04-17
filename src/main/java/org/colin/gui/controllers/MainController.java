package org.colin.gui.controllers;

import org.colin.actions.FileIntent;
import org.colin.actions.FileReceiver;
import org.colin.gui.models.AuditModel;
import org.colin.gui.views.AuditView;
import org.colin.gui.views.MainView;
import org.colin.res.IconLoader;

import java.io.File;

import static org.colin.res.IconNames.JAVA_FILE_ICON;

public class MainController implements FileReceiver {

    private MainView view;

    public MainController(MainView view) {
        this.view = view;
        this.view.setFileReceiver(this);
    }

    @Override
    public void receiveFile(File file, FileIntent intent) {
        switch (intent) {
            case OPEN: {
                AuditModel model = new AuditModel(file);
                AuditView view = new AuditView(model);
                AuditController controller = new AuditController(model, view);

                this.view.addAuditTab(file.getName(), view);
                this.view.focusLastTab();
                break;
            }
        }
    }
}
