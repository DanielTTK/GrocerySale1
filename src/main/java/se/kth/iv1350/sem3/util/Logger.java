package se.kth.iv1350.sem3.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Log class, responsible for the log.
 */
public class Logger {
    private static final String LOG_FILE_NAME = "sem3-log.txt";
    private PrintWriter logPrint;

    /**
     * Initiates the logger.
     * 
     */
    public Logger() {
        try {
            logPrint = new PrintWriter(new FileWriter(LOG_FILE_NAME, true), true);
        } catch (IOException e) {
            System.err.println("ERR: Cannot log!");
        }
    }

    /**
     * Logs a particular exception.
     * 
     * @param ex the exception
     */
    public void logException(Exception ex) {
        logPrint.println(time() + " | Exception: " + ex.getMessage());
        ex.printStackTrace();
    }

    /**
     * Logs a message, string.
     * 
     * @param msg the string
     */
    public void logMessage(String msg) {
        logPrint.println(msg);
    }

    /**
     * Creates a time in the format: yyyy-MM-dd HH:mm:ss
     * 
     * @return returns time in string format
     */
    private String time() {
        LocalDateTime saleTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return formatter.format(saleTime);
    }
}
