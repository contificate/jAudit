package org.colin.actions;

import org.colin.res.IconLoader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;

import static org.colin.res.IconNames.*;

public class OpenFileAction extends AbstractAction {

    private FileReceiver parent;

    public OpenFileAction(FileReceiver parent) {
        super("Open file(s)", IconLoader.loadIcon(JAVA_FILE_ICON));
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFrame frame = null;
        if (parent instanceof JFrame)
            frame = (JFrame) parent;

        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);

        // TODO
        // chooser.setCurrentDirectory(new File("/home/dosto/tests"));
        //

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Java source files", "java");
        chooser.setFileFilter(filter);

        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            for (final File file : chooser.getSelectedFiles()) {
                parent.receiveFile(file, FileIntent.OPEN);
            }
        }
    }
}
