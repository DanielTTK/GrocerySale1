package se.kth.iv1350.sem3.view;

/**
 * shows total income recieved through observers. Prints to client
 */
public class TotalRevenueView extends TotalRevenueTemplate {
    protected void printIncome() throws Exception {
        System.out.println("Updated total income since program start: " + totalRevenue + " SEK (incl VAT)");
    }

    @Override
    protected void reactToError(Exception e) {
        System.err.println("ERROR: Cannot display income to console!");
    }
}
