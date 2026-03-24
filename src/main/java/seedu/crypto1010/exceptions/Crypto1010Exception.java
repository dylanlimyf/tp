package seedu.crypto1010.exceptions;

/**
 * Custom exception type used throughout the Crypto1010 application.
 * Allows user-friendly and personality-filled error messages.
 */
public class Crypto1010Exception extends Exception {
    /**
     * Creates a new {@code Crypto1010Exception} with the given message.
     *
     * @param message detail message describing the error
     */
    public Crypto1010Exception(String message) {
        super(message);
    }

    /**
     * Prints an error message to the console.
     *
     * @param message the error message to print
     */
    public static void printError(String message) {
        System.out.println(message);
    }
}
