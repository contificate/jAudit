package org.colin.gui.controllers;

import org.colin.gui.models.ParseProblemModel;
import org.colin.gui.views.ParseProblemView;

import javax.swing.table.DefaultTableModel;
import java.io.FileReader;
import java.io.IOException;

/**
 * Controller used by the {@link ParseProblemView} view.
 */
public class ParseProblemController {
    /**
     * Model used for updating the view.
     */
    private ParseProblemModel model;

    /**
     * View that the controller is responsible for updating
     */
    private ParseProblemView view;

    /**
     * Create audit controller to cohere both model and view.
     *
     * @param model model that updates what's shown by {@link ParseProblemView}
     * @param view  view that invokes controller's actions
     */
    public ParseProblemController(ParseProblemModel model, ParseProblemView view) {
        this.model = model;
        this.view = view;

        readFile();
        initModel();
        highlightErrors();
    }

    /**
     * Read working file from model into source code text-area
     */
    private void readFile() {
        try {
            view.getTextArea().read(new FileReader(model.getWorkingFile()), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initModel() {
        DefaultTableModel tableModel = model.getTableModel();
        tableModel.addColumn(view.getLocalised("location"));
        tableModel.addColumn(view.getLocalised("error"));
        view.getTable().setModel(tableModel);
    }


    private void highlightErrors() {
        model.getProblems().forEach(p -> p.getLocation().ifPresent(range -> range.toRange().ifPresent(errorRange -> {
            view.highlightRange(errorRange);
            model.addErrorRow(errorRange, p.getMessage());
        })));
    }

}
