package org.colin.gui.graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GraphView extends JPanel {

    private GraphCanvas canvas;
    private JScrollPane scrollPane;

    public GraphView() {
        super(new BorderLayout());
        canvas = new GraphCanvas();
        scrollPane = new JScrollPane(canvas);
        add(scrollPane, BorderLayout.CENTER);
        initListeners();
        setAutoscrolls(true);
    }

    public void drawElements(final ArrayList<Object> objects) {
        canvas.drawElements(objects);
        SwingUtilities.invokeLater(() -> {
            scrollPane.revalidate(); // recalculate scrollbar bounds
            scrollPane.repaint(); // repaint
        });
    }

    private void initListeners() {
        MouseAdapter scrollAdapter = new MouseAdapter() {
            private Point origin;

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                super.mousePressed(mouseEvent);
                origin = mouseEvent.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                super.mouseDragged(mouseEvent);
                if (origin != null) {
                    final JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, canvas);
                    if (viewPort != null) {
                        Rectangle rect = viewPort.getViewRect();
                        rect.x += (origin.x - mouseEvent.getY());
                        rect.y += (origin.y - mouseEvent.getY());

                        canvas.scrollRectToVisible(rect);
                    }
                }
            }
        };

        canvas.addMouseListener(scrollAdapter);
        canvas.addMouseMotionListener(scrollAdapter);
    }

}
