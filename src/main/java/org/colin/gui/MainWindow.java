package org.colin.gui;

import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.github.javaparser.metamodel.DerivedProperty;
import org.colin.actions.*;
import org.colin.gui.controllers.AuditController;
import org.colin.gui.models.AuditModel;
import org.colin.gui.views.AuditView;
import org.colin.main.Main;
import org.colin.res.IconLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ResourceBundle;

import static org.colin.res.IconNames.*;

@Deprecated
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
    }

    private void initMenu() {
        menuBar = new WebMenuBar();

        WebMenu fileMenu = new WebMenu(rb.getString("file"), IconLoader.loadIcon(FILE_ICON));

        OpenFileAction open = new OpenFileAction(this);
        open.putValue("Name", rb.getString("open"));
        fileMenu.add(open);


        JMenu loadMenu = new WebMenu(rb.getString("open_from"), IconLoader.loadIcon(DOWNLOAD_ICON));
        // TOOD: add options
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
                    if (comp instanceof AuditView) {
                        // TODO: issue closing signal
                    }
                }
            }
        });
    }

    /**
     * Initialise status bar which shows JVM memory usage
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

    @Override
    public void receiveFile(File file, FileIntent intent) {
        switch (intent) {
            case OPEN: {
                AuditModel model = new AuditModel(file);
                AuditView view = new AuditView(model);
                AuditController controller = new AuditController(model, view);
                tabPane.addTab(file.getName(), IconLoader.loadIcon(JAVA_FILE_ICON), view);
                tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
                break;
            }
        }
    }

    @Override
    public void closeRequested() {

    }

}
