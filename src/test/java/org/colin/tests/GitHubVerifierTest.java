package org.colin.tests;

import org.colin.actions.verifiers.GitHubVerifier;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Input validation tests for {@link GitHubVerifier}
 */
public class GitHubVerifierTest {

    /**
     * Verifier to be used in tests
     */
    private GitHubVerifier verifier;

    /**
     * Invalid input cases
     */
    private static final String[] invalidSet = {
            "",
            "aaa",
            "google.com",
            "http://github.com/",
            "https://github.com/",
            "https://github.com/user",
            "https://github.com/user/",
            "https://github.com/user//",
            "https://github.com/user/repo/",
            "https://github.com/user/repo/file",
            "https://github.com/user/repo/file",
            "https://github.com/user/repo/.java",
            "https://github.com/user/repo/File.java dangling",
    };

    /**
     * Valid input cases
     */
    private static final String[] validSet = {
            "http://github.com/user/repo/file.java",
            "https://github.com/user/repo/file.java",
            " https://github.com/user/repo/file.java",
            "https://github.com/user/repo/file.java ",
            " https://github.com/user/repo/file.java ",
            " http://github.com/user/repo/file.java",
    };

    /**
     * Initialise instance of {@link GitHubVerifierTest#verifier} before running test cases.
     */
    @Before
    public void setUp() {
        verifier = new GitHubVerifier();
    }

    /**
     * Test that expected cases pass
     */
    @Test
    public void verifyInput() {
        // assert that all expected-to-fail cases fail
       for(String input : invalidSet)
           assertFalse("Assertion that input is invalid!", verifier.verifyInput(input));

       // assert that all expected to pass cases pass
        for(String input : validSet)
            assertTrue("Assertion that input is valid", verifier.verifyInput(input));
    }
}