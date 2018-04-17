package org.colin.gui.views;

import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.sun.istack.internal.NotNull;
import org.colin.actions.CloseAction;
import org.colin.actions.FileReceiver;
import org.colin.actions.OpenFileAction;
import org.colin.main.Main;
import org.colin.res.IconLoader;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

import static org.colin.res.IconNames.*;

public class MainView extends JFrame {

    private JPanel layout;
    private WebMenuBar menuBar;
    private JTabbedPane tabPane;
    private WebStatusBar statusBar;

    private OpenFileAction openFileAction;

    private ResourceBundle rb = ResourceBundle.getBundle(getClass().getSimpleName(), Main.locale);

    public MainView() {
        super("jAudit");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initMenu();
        initComponents();
        initStatusBar();
        //initListeners();

        setVisible(true);
    }

    private void initMenu() {

        menuBar = new WebMenuBar();

        WebMenu fileMenu = new WebMenu(rb.getString("file"), IconLoader.loadIcon(FILE_ICON));

        openFileAction = new OpenFileAction(null);
        openFileAction.putValue("Name", rb.getString("open"));
        fileMenu.add(openFileAction);

        JMenu loadMenu = new WebMenu(rb.getString("open_from"), IconLoader.loadIcon(DOWNLOAD_ICON));
        fileMenu.add(loadMenu);

        fileMenu.addSeparator();

        CloseAction close = new CloseAction(this);
        close.putValue("Name", rb.getString("close"));
        fileMenu.add(close);
        menuBar.add(fileMenu);

        WebMenu toolMenu = new WebMenu(rb.getString("tools"), IconLoader.loadIcon(TOOLS_ICON));

        menuBar.add(toolMenu);
        setJMenuBar(menuBar);


    }

    /**
     * Initialise layout
     */
    private void initComponents() {
        layout = new JPanel(new BorderLayout());
        tabPane = new JTabbedPane();
        layout.add(tabPane, BorderLayout.CENTER);

        add(layout);
    }

    /**
     * Create status bar to show JVM memory usage
     */
    private void initStatusBar() {
        statusBar = new WebStatusBar();
        WebMemoryBar mb = new WebMemoryBar();
        mb.setIcon(IconLoader.loadIcon(JVM_ICON));
        statusBar.add(mb);
        statusBar.addSeparator();

        // place at bottom of layout
        layout.add(statusBar, BorderLayout.PAGE_END);
    }

    public void setFileReceiver(@NotNull FileReceiver receiver) {
        openFileAction.setFileReceiver(receiver);
    }

    public void addAuditTab(String title, AuditView view) {
        tabPane.addTab(title, IconLoader.loadIcon(JAVA_FILE_ICON), view);
    }

    public void focusLastTab() {
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
    }
}
