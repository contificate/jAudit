package org.colin.gui;
import javax.swing.*;
import java.util.Locale;

public class LanguageOption {

    private String language;
    private ImageIcon flag;
    private Locale locale;

    public LanguageOption(final String language, final ImageIcon flag, final Locale locale) {
        this.language = language;
        this.flag = flag;
        this.locale = locale;
    }

    public String getLanguage() {
        return language;
    }

    public ImageIcon getFlag() {
        return flag;
    }

    public Locale getLocale() {
        return locale;
    }
}
