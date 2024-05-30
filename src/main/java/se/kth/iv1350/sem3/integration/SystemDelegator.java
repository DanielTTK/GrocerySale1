package se.kth.iv1350.sem3.integration;

/**
 * Instanciates all registeries.
 */
public class SystemDelegator {
    private ItemRegistry itemRegistry = new ItemRegistry();
    private SaleRegistry saleRegistry = new SaleRegistry();

    /**
     * Gets ItemRegistry
     * 
     * @return itemRegistry
     */
    public ItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    /**
     * Gets SaleRegistry
     * 
     * @return saleRegistry
     */
    public SaleRegistry getSaleRegistry() {
        return saleRegistry;
    }
}
