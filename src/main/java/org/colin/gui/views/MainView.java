package org.colin.gui.views;

import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.sun.istack.internal.NotNull;
import org.colin.actions.*;
import org.colin.actions.verifiers.GitHubVerifier;
import org.colin.gui.controllers.RemoteLoaderController;
import org.colin.gui.models.RemoteLoaderModel;
import org.colin.main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ResourceBundle;

import static org.colin.res.IconLoader.loadIcon;
import static org.colin.res.IconNames.*;

public final class MainView extends JFrame {

    private JPanel layout;
    private JTabbedPane tabPane;

    private OpenFileAction openFileAction;

    private ResourceBundle rb = ResourceBundle.getBundle(getClass().getSimpleName(), Main.locale);

    public MainView() {
        super("jAudit");
        setSize(500, 400);
        setIconImage(loadIcon(JAUDIT_ICON).getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initMenu();
        initComponents();
        initStatusBar();

        setVisible(true);
    }

    private class GitHubAction extends AbstractAction {

        public GitHubAction() {
            super("GitHub", loadIcon(GITHUB_ICON));
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            // create remote loader model to store inputted data
            RemoteLoaderModel model = new RemoteLoaderModel();

            // create view
            RemoteLoaderView view = new RemoteLoaderView(null);

            // initialise controller with specified verifier
            new RemoteLoaderController(model, view, new GitHubVerifier());

            // show the view
            view.setVisible(true);

            URL url;
            if ((url = model.getUrl()) != null) {

                File dir;

                // ask where user wants to get save directory
                SelectDirectoryAction action = new SelectDirectoryAction(null);
                action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                dir = action.getDirectory();

                if (dir != null) {
                    // construct location to write file
                    String filename = url.getPath();
                    filename = filename.substring(filename.lastIndexOf("/"));

                    // create output file
                    File output = new File(dir + filename);

                    // attempt to download file
                    try {
                        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                        FileOutputStream fos = new FileOutputStream(output);
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                        openFileAction.getReceiver().receiveFile(output, FileIntent.OPEN);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                }
            }

        }
    }

    private void initMenu() {

        WebMenuBar menuBar = new WebMenuBar();

        WebMenu fileMenu = new WebMenu(rb.getString("file"), loadIcon(FILE_ICON));

        openFileAction = new OpenFileAction(null);
        openFileAction.putValue("Name", rb.getString("open"));
        fileMenu.add(openFileAction);

        JMenu loadMenu = new WebMenu(rb.getString("open_from"), loadIcon(DOWNLOAD_ICON));
        loadMenu.add(new GitHubAction());
        fileMenu.add(loadMenu);

        fileMenu.addSeparator();

        CloseAction close = new CloseAction(this);
        close.putValue("Name", rb.getString("close"));
        fileMenu.add(close);
        menuBar.add(fileMenu);

        WebMenu toolMenu = new WebMenu(rb.getString("tools"), loadIcon(TOOLS_ICON));

        menuBar.add(toolMenu);
        setJMenuBar(menuBar);


    }

    /**
     * Initialise layout
     */
    private void initComponents() {
        // create border layout
        layout = new JPanel(new BorderLayout());
        tabPane = new JTabbedPane();

        // position primary component - tab view - in main layout
        layout.add(tabPane, BorderLayout.CENTER);

        // set the layout
        add(layout);
    }

    /**
     * Create status bar to show JVM memory usage
     */
    private void initStatusBar() {
        // create status bar containing JVM memory bar
        WebStatusBar statusBar = new WebStatusBar();
        WebMemoryBar mb = new WebMemoryBar();

        // set the icon
        mb.setIcon(loadIcon(JVM_ICON));

        // add memory bar to status bar
        statusBar.add(mb);
        statusBar.addSeparator();

        // place at bottom of layout
        layout.add(statusBar, BorderLayout.PAGE_END);
    }

    /**
     * Register file receiver for the open file action
     *
     * @param receiver file receiver callback
     */
    public void setFileReceiver(@NotNull FileReceiver receiver) {
        openFileAction.setFileReceiver(receiver);
    }

    /**
     * Add audit tab to main tab view and focus it.
     *
     * @param title title of the tab
     * @param view  component view to be shown
     */
    public void addAuditTab(String title, AuditView view) {
        tabPane.addTab(title, loadIcon(JAVA_FILE_ICON), view);
        focusLastTab();
    }

    /**
     * Change focus to last tab.
     * Used for opening newly appended tabs.
     */
    private void focusLastTab() {
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
    }
}
