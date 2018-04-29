package org.colin.tests;

import com.alee.laf.WebLookAndFeel;
import org.colin.actions.verifiers.GitHubVerifier;
import org.colin.gui.controllers.RemoteLoaderController;
import org.colin.gui.views.RemoteLoaderView;

public class RemoteLoaderTest {
    public static void main(String[] args) {
        WebLookAndFeel.install();
        RemoteLoaderView view = new RemoteLoaderView(null);
        new RemoteLoaderController(null, view, new GitHubVerifier());
        view.setVisible(true);
    }
}
