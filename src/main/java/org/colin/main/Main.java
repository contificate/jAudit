package org.colin.main;

import com.alee.laf.WebLookAndFeel;
import org.colin.gui.MainWindow;

import javax.swing.*;
import java.util.Locale;

public class Main {
    /**
     * Supported locales
     */
    private static final Locale[] supportedLocales = {
            Locale.ENGLISH, Locale.GERMANY
    };

    /**
     * Default to English (en_GB)
     */
    public static Locale locale = supportedLocales[0];

    private static void selectLocale() {
        // get user's locale
        final Locale userLocale = Locale.getDefault();

        // select locale
        for(final Locale supported : supportedLocales) {
            if(userLocale == supported) {
                locale = supported;
                break;
            }
        }

        // testing
        locale = supportedLocales[1];
    }

    static {
        // select user locale
        selectLocale();
    }

    public static void main(String[] args) {
        // initialise look and feel
        WebLookAndFeel.install();

        // select locale
        // selectLocale();

        // queue MainWindow's creation in AWT event queue
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
