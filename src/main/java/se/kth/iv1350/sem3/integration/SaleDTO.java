package se.kth.iv1350.sem3.integration;

import java.util.List;

public class SaleDTO {
    private final String saleTime;
    private final double totalCost;
    private final double totalVAT;
    private final double paidAmount;
    private final double change;
    private final List<ItemDTO> basket;

    /**
     * Creates an object representing a Sale occurance. DTO.
     * 
     * @param saleTime
     * @param basket
     * @param totalCost
     * @param totalVAT
     * @param paidAmount
     * @param change
     */
    public SaleDTO(String saleTime, List<ItemDTO> basket, double totalCost, double totalVAT, double paidAmount,
            double change) {
        this.saleTime = saleTime;
        this.basket = basket;
        this.totalCost = totalCost;
        this.totalVAT = totalVAT;
        this.paidAmount = paidAmount;
        this.change = change;
    }

    public String getObjectSaleTime() {
        return saleTime;
    }

    public double getObjectTotalCost() { // adress in report that these don't need to be public as they are not used,
                                         // discuss it
        return totalCost;
    }

    public double getObjectTotalVAT() {
        return totalVAT;
    }

    public double getObjectPaidAmount() {
        return paidAmount;
    }

    public double getObjectChange() {
        return change;
    }

    public List<ItemDTO> getObjectBasket() {
        return basket;
    }
}
