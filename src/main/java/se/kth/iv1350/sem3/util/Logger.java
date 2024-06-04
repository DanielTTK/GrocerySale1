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
     * 
     * @throws IOException
     */
    public Logger() throws IOException {
        logPrint = new PrintWriter(new FileWriter(LOG_FILE_NAME, true), true);
    }

    public void logException(Exception ex) {
        logPrint.println(time() + " | Exception: " + ex.getMessage());
        ex.printStackTrace();
    }

    public void logMessage(String msg) {
        logPrint.println(msg);
    }

    private String time() {
        LocalDateTime saleTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return formatter.format(saleTime);
    }
}
