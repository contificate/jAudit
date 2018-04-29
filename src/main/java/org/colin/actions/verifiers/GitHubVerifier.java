package org.colin.actions.verifiers;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Verifier to confirm the validity of repository Java file URLs
 */
public final class GitHubVerifier extends RemoteVerifier {

    /**
     * GitHub's domain.
     */
    private final String GITHUB_DOMAIN = "github.com";

    /**
     * Input prompt that shows form of valid input for this verifier.
     */
    public static final String GITHUB_INPUT_PROMPT = "https://github.com/user/repo/File.java";

    /**
     * GitHub's <i>raw</i> URL that the given URL must be transposed to.
     */
    public static final String GITHUB_RAW_PATH = "raw.githubusercontent.com";

    /**
     * Java file extension.
     */
    private final String FILE_EXTENSION = ".java";

    /**
     * Length of Java file extension.
     */
    private final int FILE_EXTENSION_LENGTH = FILE_EXTENSION.length();

    /**
     * Verify given input matches form defined by this class.
     * This method is defined as abstract in the parent class an invoked from <b>super</b>.
     *
     * @param input given string (from input component - see {@link RemoteVerifier#verify(JComponent)}).
     * @return true if String is valid GitHub repo file URL
     */
    @Override
    public boolean verifyInput(String input) {
        // safety precaution
        if ((input == null) || input.isEmpty())
            return false;

        // trim input we're given
        input = input.trim();

        // check for dangling data
        String[] inputParts = input.split(" ");
        if (inputParts.length > 1)
            return false;

        // split by potential whitespace, use first part
        input = input.split(" ")[0];

        // attempt to construct a URL from user input
        try {
            // construct URL
            URL url = new URL(input);

            // ensure domain is valid
            if ((url.getHost().equals(GITHUB_DOMAIN))) {
                // get path
                final String path = url.getPath();

                // check if path is empty or less than possible length
                // where possible length is determined by
                // (shortest user name + shortest repo name + shortest valid java file name) = 11
                if (path.isEmpty() || path.length() <= 11)
                    return false;

                // get parts of path
                final String[] parts = path.split("/");

                // get how many parts
                final int length = parts.length;

                // not enough parts to be a repo URL
                if (length <= 3)
                    return false;

                // check if double "//" exists in path
                for (int i = 0; i < length; i++) {
                    if (parts[i].isEmpty() && (i > 0))
                        return false;
                }

                // get final part of path, e.g. filename
                final String lastPart = parts[length - 1];

                // check if path is well-formed and has the
                return (((lastPart.endsWith(FILE_EXTENSION)) &&
                        !(lastPart.length() <= FILE_EXTENSION_LENGTH)));
            }

        } catch (MalformedURLException e) {
            // malformed URL, invalid
            return false;
        }

        return false;
    }

}
