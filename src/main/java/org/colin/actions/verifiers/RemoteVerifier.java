package org.colin.actions.verifiers;

import javax.swing.*;

/**
 * Verifier intended for verifying remote sources given as input to GUI components.
 */
public abstract class RemoteVerifier extends InputVerifier {

    /**
     * Implementation of Java Swing's {@link InputVerifier} interface, this simply passes the content of the given
     * component to the abstract verification method that is intended to be implemented by subclass concretions.
     *
     * @param inputField input field component
     * @return result of {@link RemoteVerifier#verifyInput(String)}.
     */
    @Override
    public boolean verify(JComponent inputField) {
        return verifyInput(((JTextField) inputField).getText());
    }

    /**
     * Verify a given input string
     *
     * @param input input string
     * @return true if input matches specification defined in the implementation of subclasses
     */
    public abstract boolean verifyInput(final String input);
}
