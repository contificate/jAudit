package org.colin.gui.graph;

import java.awt.*;

/**
 * Interface for drawable graph elements.<br>
 * Designed to be used as an <i>adapting interface</i> for classes that were never intended to be drawn.<br>
 */
public interface GraphDrawable {
    /**
     * Get width of element given parent graphics context.
     * @param g parent graphics context (used for context-aware metrics)
     * @return width in pixels
     */
    int getWidth(Graphics2D g);

    /**
     * Draw element
     * @param g parent graphics context
     * @param x x-coordinate
     * @param y y-coordinate
     */
    void draw(Graphics2D g, int x, int y);

    /**
     * Set background colour
     * @param color background colour
     */
    void setBackground(Color color);
}
