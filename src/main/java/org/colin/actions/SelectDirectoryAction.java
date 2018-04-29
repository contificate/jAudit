package org.colin.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class SelectDirectoryAction extends AbstractAction {

    private Component parent;
    private File directory;

    public SelectDirectoryAction(Component parent) {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // create a file chooser
        JFileChooser chooser = new JFileChooser();

        // allow user to only select directories
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        // show file opener
        int option = chooser.showOpenDialog(parent);
        directory = chooser.getCurrentDirectory();

        // if they've tried to select a directory, check if it can be written to
        if (option == JFileChooser.APPROVE_OPTION) {
            boolean canWrite = directory.canWrite();

            // while the directory can't be written to, query the user for another directory
            while (!(canWrite)) {
                JOptionPane.showMessageDialog(parent, "Can't write there");
                option = chooser.showOpenDialog(parent);

                // if the user wants to cancel the action, nullify then break
                if(option != JFileChooser.APPROVE_OPTION || directory == null) {
                    directory = null;
                    break;
                }

                // set the directory
                directory = chooser.getCurrentDirectory();

                // manipulate loop condition to loop again if the amended directory still can't be written to
                canWrite = directory.canWrite();
            }
        }

    }


    public File getDirectory() {
        return directory;
    }
}

