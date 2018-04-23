package org.colin.gui;

import javax.swing.*;

/**
 * Interface for tree nodes to implement to allow renders to draw them to their liking
 */
public interface TreeDrawable {
    /**
     * Get (possibly HTML-formatted) component label
     *
     * @return return tree component label
     */
    String getText();

    /**
     * Get component's icon
     *
     * @return return tree component icon
     */
    ImageIcon getIcon();
}
