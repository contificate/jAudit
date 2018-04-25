package org.colin.gui.models;

import org.colin.gui.LanguageOption;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Language selection model used as the model for both the view and its primary component: {@link org.colin.gui.views.LangSelectionView#optionsBox}
 */
public class LangSelectionModel implements ComboBoxModel<LanguageOption> {

    /**
     * Container of options (stores {@link LanguageOption}s)
     */
    private ArrayList<LanguageOption> options = new ArrayList<>();

    /**
     * Index of selected option
     */
    private int selectedIndex = -1;

    /**
     * Initialise model with (potentially multiple) language options
     *
     * @param options array of language options ({@link LanguageOption})
     */
    public LangSelectionModel(LanguageOption[] options) {
        // to avoid exceptions if null is passed into this constructor (since there is no provided default constructor for this class)
        if (options == null)
            return;

        this.options.addAll(Arrays.asList(options));
    }

    /**
     * Add single language option ({@link LanguageOption}).
     *
     * @param option option to be added to model
     */
    public void addOption(LanguageOption option) {
        options.add(option);
    }

    /**
     * Set selected item
     *
     * @param o item to be selected, passed in by component displaying the items.
     */
    @Override
    public void setSelectedItem(Object o) {
        // type-checking to avoid suspicious call to indexOf
        if (o instanceof LanguageOption)
            selectedIndex = options.indexOf(o);
    }

    /**
     * Get the selected item
     *
     * @return selected item
     */
    @Override
    public Object getSelectedItem() {
        return ((selectedIndex >= 0) ? options.get(selectedIndex) : null);
    }

    /**
     * Get the amount of options
     *
     * @return size of {@link LangSelectionModel#options}
     */
    @Override
    public int getSize() {
        return options.size();
    }

    /**
     * Get option ({@link LanguageOption}) at {@code i} - used by component that utilises this model.
     *
     * @param i index of option
     * @return option at {@code i}
     */
    @Override
    public LanguageOption getElementAt(int i) {
        return options.get(i);
    }

    /**
     * Get the selected locale to use
     *
     * @return selected locale ({@link LanguageOption#getLocale()})
     */
    public Locale getSelectedLocale() {
        return options.get(selectedIndex).getLocale();
    }

    @Override
    public void addListDataListener(ListDataListener listDataListener) {
        // unused but required part of interface contract
    }

    @Override
    public void removeListDataListener(ListDataListener listDataListener) {
        // unused but required part of interface contract
    }
}
