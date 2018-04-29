package org.colin.actions;

import com.sun.istack.internal.NotNull;
import org.colin.res.IconLoader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;

import static org.colin.res.IconNames.*;

/**
 * File action that calls back to an injected {@link FileReceiver}
 */
public class OpenFileAction extends AbstractAction {
    private FileReceiver parent;

    /**
     * Initialise the file action with the file receiver.
     *
     * @param parent file receiver
     */
    public OpenFileAction(FileReceiver parent) {
        super("Open file(s)", IconLoader.loadIcon(JAVA_FILE_ICON));
        this.parent = parent;
    }

    /**
     * Set the file receiver
     *
     * @param receiver callback receiver for accepting files
     */
    public void setFileReceiver(@NotNull FileReceiver receiver) {
        parent = receiver;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFrame frame = null;
        if (parent instanceof JFrame)
            frame = (JFrame) parent;

        JFileChooser chooser = new JFileChooser();

        // allow multiple files to be selected
        chooser.setMultiSelectionEnabled(true);

        // TODO remove
        chooser.setCurrentDirectory(new File("/home/dosto/tests"));
        //

        // set file opening filter
        final FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Java source files", "java");
        chooser.setFileFilter(filter);

        //
        if ((chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
                && (parent != null)) {
            for (final File file : chooser.getSelectedFiles())
                parent.receiveFile(file, FileIntent.OPEN);
        }
    }

    /**
     * Get the file receiver used by this action
     *
     * @return file receiver registered by this class
     */
    public FileReceiver getReceiver() {
        return parent;
    }
}
