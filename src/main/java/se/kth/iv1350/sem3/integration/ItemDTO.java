package se.kth.iv1350.sem3.integration;

public class ItemDTO {
    private final String itemIdentifier;
    private final String name;
    private final String description;
    private final int quantity;
    private final double cost;
    private final double VATmultiplier;

    /*
     * Creates a new instance representing a particular item.
     * 
     * @param itemIdentifier
     * 
     * @param name
     * 
     * @param description
     * 
     * @param quantity
     * 
     * @param cost
     * 
     * @param VATmultiplier
     */
    public ItemDTO(String itemIdentifier, String name, String description, int quantity, double cost,
            double VATmultiplier) {
        this.itemIdentifier = itemIdentifier;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.cost = cost;
        this.VATmultiplier = VATmultiplier;
    }

    public String getID() {
        return itemIdentifier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getCost() {
        return cost;
    }

    public double getVAT() {
        return VATmultiplier;
    }
}
