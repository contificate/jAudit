package org.colin.actions;

import org.colin.res.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import static org.colin.res.IconNames.CLOSE_ICON;

public class CloseAction extends AbstractAction {

    protected JFrame parent;

    public CloseAction(JFrame window) {
        super("Close", IconLoader.loadIcon(CLOSE_ICON));
        parent = window;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
    }
}
