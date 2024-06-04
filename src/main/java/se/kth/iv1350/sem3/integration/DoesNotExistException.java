package se.kth.iv1350.sem3.integration;

/**
 * 
 */
public class DoesNotExistException extends Exception {
    private String itemID;

    /**
     * Instanciates with a message specifiying what item does not exist. Maybe it
     * ran out.
     */
    public DoesNotExistException(String itemID) {
        super("Cannot recognize identifier " + itemID + ", since it does not exist.");
        this.itemID = itemID;
    }

    /**
     * @return Item that does not exist.
     */
    public String getItemID() {
        return itemID;
    }
}
