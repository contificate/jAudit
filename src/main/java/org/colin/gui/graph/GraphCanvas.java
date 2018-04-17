package org.colin.gui.graph;

import com.github.javaparser.ast.expr.SimpleName;
import org.colin.util.ColourUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphCanvas<T extends GraphDrawable> extends JPanel {
    private ArrayList<T> vertices = new ArrayList<>();

    private static final int PADDING = 30;
    private static final int PADDING_WIDTH = 20;
    private static final int PADDING_HEIGHT = 30;

    private int prevX = 0;

    public GraphCanvas() {
        setDoubleBuffered(true);
        setAutoscrolls(true);
    }

    public void setVertices(ArrayList<T> vertices) {
        prevX = 0;
        this.vertices = vertices;
    }

    private void drawHorizontalArrow(Graphics2D g, int x, int x1, int y) {
        g.setStroke(new BasicStroke(2));
        g.drawLine(x + 1, y, x + 30, y);
        g.drawLine((x + 20), y - 5, x + 30, y);
        g.drawLine((x + 20), y + 5, x + 30, y);
    }

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

        // iterate over vertices, pass graphics context to let them draw themselves
        int len = vertices.size();
        for(int i = 0; i < len; i++) {
            // get drawable vertex
            GraphDrawable vertex = vertices.get(i);

            // draw
            vertex.draw(graphics, x, 40);

            // add vertex width to
            x += vertex.getWidth(graphics);

            int lineY = 77;

            if(i < (len - 1))
                drawHorizontalArrow(graphics, x, (x += PADDING), lineY);

            if(i == (len - 1))
                vertex.setBackground(ColourUtil.fromHex("#dddddd"));

            prevX = (x + PADDING);
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(prevX + PADDING, 150);
    }

}
