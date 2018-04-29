package org.colin.gui;

import javax.swing.*;
import java.util.Locale;

/**
 * Class represents a language option, used by {@link org.colin.gui.views.LangSelectionView}
 */
public class LanguageOption {
    /**
     * The language being represented
     */
    private String language;

    /**
     * The flag icon for the language's region
     */
    private ImageIcon flag;

    /**
     * The locale for the language, used for localisation
     */
    private Locale locale;

    /**
     * Initialise the <b>immutable</b> language option.
     *
     * @param language language string e.g. English, Deutsch, etc.
     * @param flag     flag icon for the language's region
     * @param locale   the locale being represented by the option
     */
    public LanguageOption(final String language, final ImageIcon flag, final Locale locale) {
        this.language = language;
        this.flag = flag;
        this.locale = locale;
    }

    /**
     * Get the language string
     *
     * @return language string
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Get the flag
     *
     * @return the language icon
     */
    public ImageIcon getFlag() {
        return flag;
    }

    /**
     * Get the locale
     *
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }
}
