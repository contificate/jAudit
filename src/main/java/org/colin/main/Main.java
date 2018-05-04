package org.colin.main;

import com.alee.laf.WebLookAndFeel;
import org.colin.actions.SelectDirectoryAction;
import org.colin.db.DBConnection;
import org.colin.db.GlobalRegistry;
import org.colin.db.migrations.CreateAuditTable;
import org.colin.db.migrations.CreateFileTable;
import org.colin.gui.LanguageOption;
import org.colin.gui.controllers.AuditorController;
import org.colin.gui.controllers.LangSelectionController;
import org.colin.gui.controllers.MainController;
import org.colin.gui.models.AuditorModel;
import org.colin.gui.models.LangSelectionModel;
import org.colin.gui.views.AuditorView;
import org.colin.gui.views.LangSelectionView;
import org.colin.gui.views.MainView;
import org.sqlite.core.DB;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

import static org.colin.db.GlobalConstants.DB_PATH;
import static org.colin.res.IconLoader.loadIcon;
import static org.colin.res.IconNames.GERMANY_ICON;
import static org.colin.res.IconNames.UK_ICON;

public class Main {

    /**
     * Language options for jAudit
     */
    private static final LanguageOption[] languageOptions = {
            new LanguageOption("English (GB)", loadIcon(UK_ICON), Locale.ENGLISH),
            new LanguageOption("Deutsch", loadIcon(GERMANY_ICON), Locale.GERMANY)
    };

    /**
     * Default to English (en_GB)
     */
    public static Locale locale = languageOptions[0].getLocale();

    /**
     * Show language selection dialog and set the locale based on user's choice
     */
    private static void selectLocale() {
        // initialise model with supported locales
        LangSelectionModel model = new LangSelectionModel(languageOptions);

        // create dialog view
        LangSelectionView view = new LangSelectionView(null);

        // initialise controller
        new LangSelectionController(model, view);

        // show the view, blocks
        view.setVisible(true);

        // set user-selected locale
        locale = model.getSelectedLocale();
    }

    /**
     * Entry point
     *
     * @param args TODO
     */
    public static void main(String[] args) {
        // initialise look and feel
        WebLookAndFeel.install();

        // get global registry
        GlobalRegistry registry = GlobalRegistry.getInstance();

        // attempt to get database file as startup argument
        if(args.length >= 1) {
            // put first argument
            registry.put(DB_PATH, args[0]);
        } else {
            // make them choose a directory to create the database at
            SelectDirectoryAction action = new SelectDirectoryAction(null);
            action.actionPerformed(new ActionEvent(action, ActionEvent.ACTION_PERFORMED, null));

            // get selected directory (or null)
            final File directory = action.getDirectory();

            // if directory is null, user cancelled action, exit.
            if(directory == null || !(directory.isDirectory())) {
                System.err.println("Can't execute without chosen database location.");
                return;
            }

            // register database path
            registry.put(DB_PATH, directory + "/jaudit.sqlite");
        }

        System.err.println(registry.get(DB_PATH));

        // TODO: remove
        DBConnection db = DBConnection.getInstance();
        if(db.isConnected()) {
            new CreateFileTable(db).up();
            new CreateAuditTable(db).up();
        }

        // allow user to select locale
        selectLocale();

        // queue MainView's creation in AWT event queue
        SwingUtilities.invokeLater(() -> {
            MainView view = new MainView();
            new MainController(view);
        });
    }
}
