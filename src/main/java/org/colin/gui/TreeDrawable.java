package org.colin.gui;

import javax.swing.ImageIcon;

/**
 * Interface for tree nodes to implement to allow renders to draw them to their liking
 */
public interface TreeDrawable {
    /**
     * Get (possibly HTML-formatted) component label
     * @return
     */
    String getText();

    /**
     * Get component's icon
     * @return
     */
    ImageIcon getIcon();
}
