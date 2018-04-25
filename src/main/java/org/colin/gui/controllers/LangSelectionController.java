package org.colin.gui.controllers;

import org.colin.gui.LangListRenderer;
import org.colin.gui.LanguageOption;
import org.colin.gui.models.LangSelectionModel;
import org.colin.gui.views.LangSelectionView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller used by the {@link LangSelectionView} view.
 */
public class LangSelectionController implements ActionListener {
    /**
     * Model used for updating the view.
     */
    private LangSelectionModel model;

    /**
     * Auditing view used to invoke controller.
     */
    private LangSelectionView view;


    /**
     * Create audit controller to cohere both model and view.
     *
     * @param model model that updates what's shown by {@link LangSelectionModel}
     * @param view  view that invokes controller's actions
     */
    public LangSelectionController(LangSelectionModel model, LangSelectionView view) {
        this.model = model;
        this.view = view;

        initModel();
        initListener();
    }

    private void initModel() {
        final JComboBox<LanguageOption> comboBox = view.getOptionsBox();
        comboBox.setModel(model);
        comboBox.setRenderer(new LangListRenderer());
    }

    private void initListener() {
        view.getOptionsBox().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        view.setVisible(false);
        view.dispose();
    }
}
