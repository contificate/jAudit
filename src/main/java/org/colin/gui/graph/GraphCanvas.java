package org.colin.gui.graph;

import org.colin.util.ColourUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Canvas used to draw the {@link GraphDrawable} elements.
 *
 * @param <T> {@link GraphDrawable} item type
 */
public class GraphCanvas<T extends GraphDrawable> extends JPanel {
    /**
     * List of vertices to be drawn
     */
    private ArrayList<T> vertices = new ArrayList<>();

    /**
     * General padding
     */
    private static final int PADDING = 30;

    /**
     * Width padding
     */
    private static final int PADDING_WIDTH = 20;

    /**
     * Height padding
     */
    private static final int PADDING_HEIGHT = 30;

    /**
     * Previous - cached - x-coordinate used to retain state since each invocation
     * of {@link GraphCanvas#paint(Graphics)} will yield a different graphics context.
     */
    private int prevX = 0;

    /**
     * Initialised graph canvas.
     * Use an offscreen buffer for painting (double buffer).
     */
    public GraphCanvas() {
        setDoubleBuffered(true);
        setAutoscrolls(true);
    }

    public void setVertices(ArrayList<T> vertices) {
        prevX = 0;
        this.vertices = vertices;
    }

    /**
     * Draw horizontal arrow
     *
     * @param g  graphics context
     * @param x  x-coordinate
     * @param x1 second x-coordinate
     * @param y  y-coordinate
     */
    private void drawHorizontalArrow(Graphics2D g, int x, int x1, int y) {
        g.setStroke(new BasicStroke(2));
        g.drawLine(x + 1, y, x + PADDING, y);
        g.drawLine((x + PADDING_WIDTH), (y - 5), (x + PADDING), y);
        g.drawLine((x + PADDING_WIDTH), (y + 5), (x + PADDING), y);
    }

    /**
     * Paint the canvas, e.g. paint all of the vertices.
     *
     * @param g parent graphics context
     */
    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);

        // get 2d graphics context
        Graphics2D graphics = (Graphics2D) g;

        // enable anti aliasing for shapes and text
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // begin offset from padding
        int x = PADDING;

        // get vertices count
        final int len = vertices.size();
        final int lastIndex = (len - 1);

        // iterate over vertices, pass graphics context to let them draw themselves
        for (int i = 0; i < len; i++) {
            // get drawable vertex
            GraphDrawable vertex = vertices.get(i);

            // draw the vertex
            vertex.draw(graphics, x, (PADDING_HEIGHT + 10));

            // add vertex width to
            x += vertex.getWidth(graphics);

            // this should really be decided by viewport to center the nodes
            int lineY = ((PADDING_HEIGHT * 2) + 15);

            // if not last item, draw a directed arrow
            if (i < lastIndex)
                drawHorizontalArrow(graphics, x, (x += PADDING), lineY);

            // if last item, use different background colour (to further signify the last element is the sink vertex)
            if (i == lastIndex)
                vertex.setBackground(ColourUtil.fromHex("#dddddd"));

            // accumulate previous positions
            prevX = (x + PADDING);
        }

    }

    /**
     * Get preferred size, used by {@link GraphView} to specify draggable viewport bounds.
     *
     * @return preferred dimensions
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(prevX + PADDING, 150);
    }

}
