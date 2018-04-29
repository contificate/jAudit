package org.colin.tests;

import com.alee.laf.WebLookAndFeel;
import org.colin.gui.views.ParseProblemView;

public class ParseViewTest {

    public static void main(String[] args) {
        WebLookAndFeel.install();
        new ParseProblemView(null).setVisible(true);
    }

}
