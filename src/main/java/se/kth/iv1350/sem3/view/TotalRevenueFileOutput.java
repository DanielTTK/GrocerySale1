package se.kth.iv1350.sem3.view;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Prints total income to file
 */
public class TotalRevenueFileOutput extends TotalRevenueView {
    private final String INCOME_FILE_NAME = "INCOME-LOG.txt";
    private PrintWriter fileWrite;

    @Override
    protected void printIncome() {
        try {
            fileWrite = new PrintWriter(new FileWriter(INCOME_FILE_NAME, true), true);
        } catch (IOException e) {
            System.err.println("ERR: Cannot print income to file!");
        }

        fileWrite.println("Updated total income since program start: " + totalRevenue + " SEK (incl VAT)");
    }

    @Override
    protected void reactToError(Exception e) {
        System.err.println("ERR: Cannot print income to file!");
    }
}
