package org.colin.gui.graph;

import com.github.javaparser.ast.Node;

import java.awt.*;

/**
 * Drawable node.<br><br>
 * This class is simply an adapter pattern to allow nodes (the <i>adaptee</i>) to be drawn.<br>
 * It wraps the node ({@link Node}) and implements an interface - {@link GraphDrawable} - on its behalf.
 * This class adheres to the <a href="https://en.wikipedia.org/wiki/Open/closed_principle">Open/Closed principle</a> from
 * the SOLID principles.
 */
public class DrawableNode implements GraphDrawable {
    /**
     * Adaptee
     */
    protected final Node node;

    /**
     * Cached, trimmed, name of node.
     */
    private String label;

    /**
     * Bounds of the node (vertex).
     * Each bound is a rectangle
     */
    private Rectangle bounds = new Rectangle();

    /**
     * Background colour used when drawing
     */
    private Color backgroundColor = Color.WHITE;

    public DrawableNode(Node node) {
        this.node = node;
        setLabelFromNode();
    }

    public String getLabel() {
        return label;
    }

    protected void setLabelFromNode() {
        label = node.getClass().toString();
        label = label.substring(label.lastIndexOf(".") + 1);
    }

    /**
     * Return drawable node's width (includes font metrics for label)
     *
     * @param g parent graphics context (used for context-aware metrics)
     * @return width in pixels
     */
    @Override
    public int getWidth(Graphics2D g) {
        final FontMetrics fm = g.getFontMetrics();
        return (fm.stringWidth(label) + 30);
    }

    /**
     * Draw the node, given parent graphics context.
     * @param g parent graphics context
     * @param x x-coordinate
     * @param y y-coordinate
     */
    @Override
    public void draw(Graphics2D g, int x, int y) {
        final FontMetrics fm = g.getFontMetrics();
        final int textWidth = fm.stringWidth(label),
                textHeight = fm.getHeight();

        int rectWidth = (textWidth + 30);
        int rectHeight = (textHeight + 20);

        g.setStroke(new BasicStroke(3));
        g.drawRect(x, 60, rectWidth, rectHeight);

        bounds.x = x;
        bounds.y = 60;
        bounds.width = rectWidth;
        bounds.height = rectHeight;

        // g.setColor(Color.WHITE);
        g.setColor(backgroundColor);
        g.fillRect(x, 60, rectWidth, rectHeight);
        g.setColor(Color.BLACK);

        g.drawString(label, (x + ((rectWidth - textWidth) / 2)), 60 + ((rectHeight + 30) - textHeight) / 2);
    }

    /**
     * Set background colour
     * @param color background colour
     */
    @Override
    public void setBackground(Color color) {
        this.backgroundColor = color;
    }
}
