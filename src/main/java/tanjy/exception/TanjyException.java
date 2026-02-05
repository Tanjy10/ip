package tanjy.exception;

/**
 * An application-specific exception for the Tanjy bot.
 * Signals invalid input and/or other recoverable errors.
 */
public class TanjyException extends Exception {
    public TanjyException(String message) {
        super(message);
    }
}
