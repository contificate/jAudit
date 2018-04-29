package org.colin.gui.controllers;

import com.alee.laf.text.WebTextField;
import org.colin.actions.verifiers.GitHubVerifier;
import org.colin.actions.verifiers.RemoteVerifier;
import org.colin.gui.models.RemoteLoaderModel;
import org.colin.gui.views.RemoteLoaderView;

import javax.annotation.Nonnull;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Controller used by the {@link RemoteLoaderView} view.
 */
public class RemoteLoaderController {
    /**
     * Model used for updating the view.
     */
    private RemoteLoaderModel model;

    /**
     * Auditing view used to invoke controller.
     */
    private RemoteLoaderView view;

    /**
     * Remote verifier used for verifying input of remote targets.
     */
    private RemoteVerifier verifier;

    public RemoteLoaderController(RemoteLoaderModel model, RemoteLoaderView view, @Nonnull RemoteVerifier verifier) {
        this.model = model;
        this.view = view;
        this.verifier = verifier;

        initListeners();
    }

    private void initListeners() {
        // bind input view to verifier
        final WebTextField inputField = view.getTextField();
        inputField.setInputPrompt(GitHubVerifier.GITHUB_INPUT_PROMPT);
        inputField.setInputVerifier(verifier);

        // initialise input listener to verify input on text change
        inputField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                view.setValidInput(verifier.verify(inputField));
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                view.setValidInput(verifier.verify(inputField));
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) { }
        });

        // close the view if cancelled
        view.setOnCancel(e -> {
            // nullify
            model.setUrl(null);
            view.dispose();
        });

        // transpose the url
        view.setOnLoad(e -> {
            transposeInput();
            view.dispose();
        });
    }

    private void transposeInput() {
        try {
            // get - assumed to be validly formatted - url
            URL url = new URL(view.getTextField().getText());

            // transpose the URL
            URL githubUrl = new URL(url.getProtocol(),
                    GitHubVerifier.GITHUB_RAW_PATH,
                    url.getPath().replace("blob/", ""));

            // update model
            model.setUrl(githubUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
