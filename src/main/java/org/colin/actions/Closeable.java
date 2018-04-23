package org.colin.actions;

/**
 * Interface for closeable objects
 */
public interface Closeable {
    /**
     * Request closure
     */
    void closeRequested();
}
