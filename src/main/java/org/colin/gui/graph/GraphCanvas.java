package org.colin.gui.graph;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphCanvas extends JPanel{

    private ArrayList<Object> nodes = new ArrayList<>();
    private static final int PADDING_WIDTH = 20;
    private static final int PADDING_HEIGHT = 30;
    private int lastX = 0;

    public GraphCanvas() {
        setAutoscrolls(true);
    }

    public void drawElements(ArrayList<Object> elements) {
        lastX = 0;
        nodes = elements;
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        graphics.setBackground(Color.WHITE);

        int x = 0;
        final int len = nodes.size();
        for(int i = 0; i < len; i++) {
            final String text = nodes.get(i).toString();
            final FontMetrics fm = graphics.getFontMetrics();
            final int textWidth = fm.stringWidth(text),
                    textHeight = fm.getHeight();

            int rectWidth = (textWidth + PADDING_WIDTH);
            int rectHeight = (textHeight + PADDING_HEIGHT);

            graphics.setStroke(new BasicStroke(3));
            graphics.drawRect(30 + x, 60, rectWidth, rectHeight);
            graphics.setColor(Color.WHITE);
            graphics.fillRect(30 + x, 60, rectWidth, rectHeight);
            graphics.setColor(Color.BLACK);

            graphics.drawString(text, (30 + x) + (rectWidth - textWidth) / 2, 60 + ((rectHeight + PADDING_HEIGHT) - textHeight) / 2);

            x += (30 + rectWidth);

            if(i < (len - 1)) {
                graphics.setStroke(new BasicStroke(2));
                int lineY = 60 + (rectHeight / 2);
                graphics.drawLine(x + 1, lineY, x + 30, lineY);
                graphics.drawLine((x + 20), lineY - 5, x + 30, lineY);
                graphics.drawLine((x + 20), lineY + 5, x + 30, lineY);
            }
            lastX = x;
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(lastX + 30, 150);
    }

}
