package org.colin.actions;

import org.colin.res.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import static org.colin.res.IconNames.CLOSE_ICON;

/**
 * Close action used for dispatching window-closing events to JFrames.
 */
public class CloseAction extends AbstractAction {

    /**
     * Window that this class will dispatch a close event for.
     */
    private JFrame parent;

    /**
     * Create close action.
     *
     * @param window window to be closed by action
     */
    public CloseAction(JFrame window) {
        super("Close", IconLoader.loadIcon(CLOSE_ICON));
        parent = window;
    }

    /**
     * Dispatch window closing event
     *
     * @param actionEvent invoker's action event
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
    }
}
