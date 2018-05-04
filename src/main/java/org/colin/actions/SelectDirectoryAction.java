package org.colin.actions;

import org.colin.main.Main;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ResourceBundle;

/**
 * Select directory action and confirm user has permissions to write to selected directory.
 */
public class SelectDirectoryAction extends AbstractAction {
    /**
     * Parent component to display open file dialog relative to.
     */
    private Component parent;

    /**
     * Chosen directory.
     */
    private File directory;

    /**
     * Resource bundle for localisation.
     */
    private ResourceBundle rb = ResourceBundle.getBundle(getClass().getSimpleName(), Main.locale);

    /**
     * Construct the select directory action
     *
     * @param parent parent component to show open file dialog relative to
     */
    public SelectDirectoryAction(@Nullable Component parent) {
        this.parent = parent;
    }

    /**
     * Action performed event callback
     *
     * @param event action event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        // create a file chooser
        JFileChooser chooser = new JFileChooser();

        // allow user to only select directories
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        // show file opener
        int option = chooser.showOpenDialog(parent);
        directory = chooser.getSelectedFile();

        // if they've tried to select a directory, check if it can be written to
        if (option == JFileChooser.APPROVE_OPTION) {
            boolean canWrite = directory.canWrite();

            // while the directory can't be written to, query the user for another directory
            while (!(canWrite)) {
                // show error
                JOptionPane.showMessageDialog(parent, rb.getString("dir_error"), rb.getString("error_title"),
                        JOptionPane.ERROR_MESSAGE);

                // prompt again
                option = chooser.showOpenDialog(parent);

                // if the user wants to cancel the action, nullify then break
                if (option != JFileChooser.APPROVE_OPTION || directory == null) {
                    directory = null;
                    break;
                }

                // set the directory
                directory = chooser.getSelectedFile();

                // manipulate loop condition to loop again if the amended directory still can't be written to
                canWrite = directory.canWrite();
            }
        }

    }

    /**
     * Get the directory from the action
     *
     * @return directory
     */
    public @Nullable
    File getDirectory() {
        return directory;
    }
}

