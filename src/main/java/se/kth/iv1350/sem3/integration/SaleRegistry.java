package se.kth.iv1350.sem3.integration;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all methods that has to do with Accounting System.
 * <code>SaleRegistry</code>
 */
public class SaleRegistry {
    private List<SaleDTO> accounting = new ArrayList<>();

    public SaleRegistry() {

    }

    /**
     * Saves a sale occurance and its details in the accounting system
     */
    public void saveSale(SaleDTO currSale) {
        accounting.add(currSale);
    }

    /**
     * Gets the whole accounting system, sale registry from
     * <code>saleRegistry</code>.
     * 
     * @return
     */
    public List<SaleDTO> getSaleRegistryList() {
        return accounting;
    }
}
