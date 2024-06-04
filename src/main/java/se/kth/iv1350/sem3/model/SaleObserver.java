package se.kth.iv1350.sem3.model;

import se.kth.iv1350.sem3.integration.SaleDTO;

/**
 * 
 */
public interface SaleObserver {
    /**
     * Called when transaction has been made. When sale has been paid.
     * 
     * @param soldItem The item that was sold.
     */
    void finishedSale(SaleDTO finishedSale);
}
