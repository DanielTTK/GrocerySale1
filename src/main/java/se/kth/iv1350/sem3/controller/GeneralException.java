package se.kth.iv1350.sem3.controller;

/**
 * Thrown for general program failures where cause is unknown.
 */
public class GeneralException extends Exception {
    /**
     * Creates exception instance with message and original exception.
     * 
     * @param msg
     * @param originalException
     */
    public GeneralException(String msg, Exception originalException) {
        super(msg, originalException);
    }
}
