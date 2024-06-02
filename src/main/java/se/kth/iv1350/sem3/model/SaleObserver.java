package se.kth.iv1350.sem3.model;

import se.kth.iv1350.sem3.integration.ItemDTO;

/**
 * 
 */
public interface SaleObserver {
    /**
     * Called when transaction has been made. When sale has been paid.
     * 
     * @param soldItem The item that was sold.
     */
    void finishedSale(ItemDTO soldItem);
}
