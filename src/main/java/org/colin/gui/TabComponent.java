package org.colin.gui;

import org.colin.res.IconLoader;

import javax.swing.*;
import java.awt.*;

@Deprecated
public class TabComponent extends JPanel {
    public TabComponent() {
        JLabel txt = new JLabel("Tab");
        txt.setFont(new Font("System", Font.PLAIN, 9));
        add(txt);

        JButton closeButton = new JButton(IconLoader.loadIcon("close.png"));
        closeButton.setBorder(null);
        closeButton.setContentAreaFilled(false);
        closeButton.setFont(new Font("System", Font.PLAIN, 9));
        add(closeButton);

        setOpaque(true);
    }
}
