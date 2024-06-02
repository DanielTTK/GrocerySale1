package se.kth.iv1350.sem3.integration;

import se.kth.iv1350.sem3.model.Amount;

public class ItemDTO {
    private final String itemIdentifier;
    private final String name;
    private final String description;
    private final int quantity;
    private final Amount cost;
    private final double VATmultiplier;

    /**
     * Creates an object representing a particular item.
     * 
     * @param itemIdentifier
     * @param name
     * @param description
     * @param quantity
     * @param cost
     * @param VATmultiplier
     */
    public ItemDTO(String itemIdentifier, String name, String description, int quantity,
            Amount cost, double VATmultiplier) {
        this.itemIdentifier = itemIdentifier;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.cost = cost;
        this.VATmultiplier = VATmultiplier;
    }

    /**
     * Gets item ID
     * 
     * @return itemIdentifier
     */
    public String getID() {
        return itemIdentifier;
    }

    /**
     * Gets item name
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets item description
     * 
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets item quantity
     * 
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets item cost
     * 
     * @return cost
     */
    public Amount getCost() {
        return cost;
    }

    /**
     * Gets item VAT multiplier
     * 
     * @return VATmultiplier
     */
    public double getVAT() {
        return VATmultiplier;
    }
}
