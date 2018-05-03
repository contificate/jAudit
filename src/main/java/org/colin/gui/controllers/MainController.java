package org.colin.gui.controllers;

import org.colin.actions.FileIntent;
import org.colin.actions.FileReceiver;
import org.colin.gui.models.AuditModel;
import org.colin.gui.views.AuditView;
import org.colin.gui.views.MainView;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Controller used by the {@link MainView} view.
 */
public class MainController implements FileReceiver, DropTargetListener {
    /**
     * View that the controller is responsible for updating
     */
    private MainView view;

    /**
     * Intermediate variable to transfer file(s) from {@link MainController#dragEnter(DropTargetDragEvent)}
     * to {@link MainController}
     * <p>
     * This value is fed to {@link MainController#receiveFile(File, FileIntent)} with the <b>{@link FileIntent#OPEN}</b>
     * intent.
     */
    private File openingFile;

    /**
     * Initialise primary view
     *
     * @param view injected view
     */
    public MainController(MainView view) {
        this.view = view;

        // act as file receiver on behalf of view
        this.view.setFileReceiver(this);

        // initialise drop target to accept opening of files via drag-drop
        initDropTarget();
    }

    private void initDropTarget() {
        new DropTarget(this.view, DnDConstants.ACTION_COPY_OR_MOVE, this);
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

                if(model.hasError()) {
                    break;
                }


                // local reference to view captured by anonymous class below
                final MainView mainView = this.view;

                // create anonymous worker that waits on ParserWorker (so we don't hang the UI thread whilst waiting on parsing)
                new SwingWorker<Boolean, Boolean>() {
                    /**
                     * Parse the files
                     * @return true if parse completed with no errors
                     */
                    @Override
                    protected Boolean doInBackground() {
                        // parse the file
                        return controller.parse();
                    }

                    /**
                     * Callback used to signify if parsing is complete
                     */
                    @Override
                    protected void done() {
                        try {
                            // if parsing was successful, open the file in an audit panel
                            if (get())
                                mainView.addAuditTab(file.getName(), view);
                        } catch (InterruptedException | ExecutionException e) {
                            // this block is unlikely to ever be reached since there's no out-of-sync threads,
                            // the opening, parsing, and construction of audit panel is built on awaited callbacks (asynchronous)
                            e.printStackTrace();
                        }
                    }
                }.execute();

                break;
            }
        }
    }

    /**
     * Drag enter event used for allowing files to be dropped for opening.
     * @param event drag enter event
     */
    @Override
    public void dragEnter(DropTargetDragEvent event) {
        // get transferable from drag event
        final Transferable transferable = event.getTransferable();

        // does the transfer event support lists of files?
        final boolean supportedAction = transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor);

        // if supported, iterate and attempt to treat data as files for opening
        if (supportedAction) {
            try {
                // get transferred data
                final Object data = transferable.getTransferData(DataFlavor.javaFileListFlavor);

                // check if data represents a list
                if (data instanceof List) {
                    // if it's a list, treat it as such
                    final List files = (List) data;

                    // iterate list and attempt to treat elements as files
                    for (Object item : files) {
                        // check if element in list is a file
                        if (item instanceof File) {
                            // get the file and its name
                            final File file = (File) item;
                            final String fileName = file.getName();

                            // check if valid java source file extension
                            if (fileName.endsWith(".java")) {
                                // set intermediary file
                                openingFile = file;

                                // accept the drag event
                                event.acceptDrag(DnDConstants.ACTION_COPY);
                            } else {
                                // reject the drag event, drop event won't occur
                                event.rejectDrag();
                            }
                        }
                    }
                }
            } catch (UnsupportedFlavorException | IOException e) {
                // reject drag if data flavour or IO exception occurs
                event.rejectDrag();
            }
        }


    }

    /**
     * Accept the dropping of data, this method is invoked by {@link MainController#dragEnter(DropTargetDragEvent)}
     * after it establishes whether the dropped data from the event contains (a) file(s).
     * @param event drop event
     */
    @Override
    public void drop(DropTargetDropEvent event) {
        if (openingFile != null) {
            receiveFile(openingFile, FileIntent.OPEN);
        }
    }

    @Override
    public void dragOver(DropTargetDragEvent dropTargetDragEvent) { }

    @Override
    public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) { }

    @Override
    public void dragExit(DropTargetEvent dropTargetEvent) { }

}
