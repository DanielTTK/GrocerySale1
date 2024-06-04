package se.kth.iv1350.sem3.view;

import se.kth.iv1350.sem3.model.Amount;
import se.kth.iv1350.sem3.model.SaleObserver;

import se.kth.iv1350.sem3.integration.SaleDTO;

/**
 * shows total income recieved through observers.
 */
public class TotalRevenueView implements SaleObserver {
    private Amount totalRevenue;

    public TotalRevenueView() {
        totalRevenue = new Amount(0);
    }

    @Override
    public void finishedSale(SaleDTO finishedSale) {
        totalRevenue = totalRevenue.addAmt(finishedSale.getSaleTotalCost().mathFloor());
        printIncome();
    }

    private void printIncome() {
        System.out.println("Updated total income since program start: " + totalRevenue + " SEK (incl VAT)");
    }
}
