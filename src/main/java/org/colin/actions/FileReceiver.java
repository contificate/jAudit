package org.colin.actions;

import java.io.File;

/**
 * Interface for objects to receive files to work with, alongside intent in message.
 */
public interface FileReceiver {
    /**
     * Receive a file
     * @param file file being received
     * @param intent intention with file (open, etc.)
     */
    void receiveFile(File file, final FileIntent intent);
}
