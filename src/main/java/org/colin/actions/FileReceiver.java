package org.colin.actions;

import java.io.File;

public interface FileReceiver {
    void receiveFile(File file, final FileIntent intent);
}
