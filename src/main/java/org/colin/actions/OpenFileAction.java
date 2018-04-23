package org.colin.actions;

import com.sun.istack.internal.NotNull;
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

    public void setFileReceiver(@NotNull FileReceiver receiver) {
        parent = receiver;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFrame frame = null;
        if (parent instanceof JFrame)
            frame = (JFrame) parent;

        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true); // allow multiple files to be selected

        // TODO remove
        chooser.setCurrentDirectory(new File("/home/dosto/tests"));
        //

        final FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Java source files", "java");
        chooser.setFileFilter(filter);

        if ((chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
                && (parent != null)) {
            for (final File file : chooser.getSelectedFiles())
                parent.receiveFile(file, FileIntent.OPEN);
        }
    }
}
