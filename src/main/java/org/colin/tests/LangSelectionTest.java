package org.colin.tests;

import org.colin.gui.LanguageOption;
import org.colin.gui.controllers.LangSelectionController;
import org.colin.gui.models.LangSelectionModel;
import org.colin.gui.views.LangSelectionView;

import java.util.Locale;

import static org.colin.res.IconLoader.loadIcon;
import static org.colin.res.IconNames.GERMANY_ICON;
import static org.colin.res.IconNames.UK_ICON;

public class LangSelectionTest {

    public static void main(String[] args) {
        LangSelectionModel model = new LangSelectionModel(null);
        model.addOption(new LanguageOption("English (GB)", loadIcon(UK_ICON), Locale.ENGLISH));
        model.addOption(new LanguageOption("Deutsch", loadIcon(GERMANY_ICON), Locale.GERMANY));
        LangSelectionView view = new LangSelectionView(null);
        LangSelectionController controller = new LangSelectionController(model, view);
        view.setVisible(true);

        System.out.println(((LanguageOption) model.getSelectedItem()).getLocale());
    }

}
