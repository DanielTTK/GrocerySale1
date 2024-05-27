package se.kth.iv1350.sem3.view;

public class ErrorDisplay {
    /**
     * Creates error message including string parameter msg.
     * 
     * @param msg error details in string form
     */
    public void writeErrorMessage(String msg) { // Has to be public for the test. Couldn't solve directory issue
        System.out.println("ERROR: " + msg);
    }
}
