package org.colin.gui.graph;

import java.awt.*;

public interface GraphDrawable {
    int getWidth(Graphics2D g);
    void draw(Graphics2D g, int x, int y);
    void setBackground(Color color);
}
