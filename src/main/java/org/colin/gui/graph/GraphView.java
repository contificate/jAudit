package org.colin.gui.graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GraphView<T extends GraphDrawable> extends JPanel {

    private GraphCanvas<T> canvas;
    private JScrollPane scrollPane;

    public GraphView() {
        super(new BorderLayout());
        canvas = new GraphCanvas<>();
        scrollPane = new JScrollPane(canvas);
        add(scrollPane, BorderLayout.CENTER);
        initListeners();
        setAutoscrolls(true);
    }


    public void setVertices(ArrayList<T> vertices) {
        canvas.setVertices(vertices);
        SwingUtilities.invokeLater(() -> {
            scrollPane.revalidate(); // recalculate scrollbar bounds
            scrollPane.repaint(); // repaint
            scrollPane.revalidate();
            scrollPane.validate();
            canvas.revalidate();
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
                    final JViewport viewPort = scrollPane.getViewport();
                    if (viewPort != null) {
                        Rectangle rect = viewPort.getViewRect();
                        rect.x += (origin.x - mouseEvent.getX());
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
