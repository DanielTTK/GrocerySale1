package se.kth.iv1350.sem3.view;

import se.kth.iv1350.sem3.integration.SaleDTO;
import se.kth.iv1350.sem3.model.Amount;
import se.kth.iv1350.sem3.model.SaleObserver;

abstract class TotalRevenueTemplate implements SaleObserver {
    Amount totalRevenue;

    public TotalRevenueTemplate() {
        totalRevenue = new Amount(0);
    }

    @Override
    public void finishedSale(SaleDTO finishedSale) {
        totalRevenue = totalRevenue.addAmt(finishedSale.getSaleTotalCost().mathFloor());
        showTotalIncome();
    }

    private void showTotalIncome() {
        try {
            printIncome();
        } catch (Exception e) {
            reactToError(e);
        }
    }

    protected abstract void printIncome() throws Exception;

    protected abstract void reactToError(Exception e);
}
