package org.colin.gui;

import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import org.colin.actions.*;
import org.colin.main.Main;
import org.colin.res.IconLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ResourceBundle;

import static org.colin.res.IconNames.*;

public class MainWindow extends JFrame implements FileReceiver, Closeable {

    private JPanel layout;
    private WebMenuBar menuBar;
    private JTabbedPane tabPane;
    private WebStatusBar statusBar;

    private ResourceBundle rb = ResourceBundle.getBundle(getClass().getSimpleName(), Main.locale);

    public MainWindow() {
        super("jAudit");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initMenu();
        initComponents();
        initStatusBar();
        initListeners();

        setVisible(true);

        // TEST
        // receiveFile(new File("/home/dosto/CompileDriver.java"), FileIntent.OPEN);
    }

    private void initMenu() {
        menuBar = new WebMenuBar();

        WebMenu fileMenu = new WebMenu(rb.getString("file"), IconLoader.loadIcon(FILE_ICON));

        OpenFileAction open = new OpenFileAction(this);
        open.putValue("Name", rb.getString("open"));
        fileMenu.add(open);


        JMenu loadMenu = new WebMenu(rb.getString("open_from"), IconLoader.loadIcon(DOWNLOAD_ICON));
        // add options
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

    private void initComponents() {
        layout = new JPanel(new BorderLayout());
        tabPane = new JTabbedPane();

        layout.add(tabPane, BorderLayout.CENTER);
        add(layout);
    }

    private void initListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                final int tabCount = tabPane.getTabCount();
                for (int i = 0; i < tabCount; i++) {
                    Component comp = tabPane.getComponentAt(i);
                    if (comp instanceof AuditPane) {
                        // TODO: issue closing signal

                    }
                }
            }
        });
    }

    private void initStatusBar() {
        statusBar = new WebStatusBar();
        WebMemoryBar mb = new WebMemoryBar();
        mb.setIcon(IconLoader.loadIcon(JVM_ICON));
        statusBar.add(mb);
        statusBar.addSeparator();
        layout.add(statusBar, BorderLayout.PAGE_END);
    }

    @Override
    public void receiveFile(File file, FileIntent intent) {
        switch (intent) {
            case OPEN: {
                tabPane.addTab(file.getName(), IconLoader.loadIcon(JAVA_FILE_ICON), new AuditPane(file));
                tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
                break;
            }
        }
    }

    @Override
    public void closeRequested() {

    }

}
