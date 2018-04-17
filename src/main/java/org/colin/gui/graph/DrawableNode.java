package org.colin.gui.graph;

import com.github.javaparser.ast.Node;

import java.awt.*;

public class DrawableNode implements GraphDrawable {
    protected final Node node;
    private String label;
    private Rectangle bounds = new Rectangle();
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

    @Override
    public int getWidth(Graphics2D g) {
        final FontMetrics fm = g.getFontMetrics();
        return (fm.stringWidth(label) + 30);
    }

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

    @Override
    public void setBackground(Color color) {
        this.backgroundColor = color;
    }
}
