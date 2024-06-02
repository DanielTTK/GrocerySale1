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

    /**
     * Gets sale time from sale
     * 
     * @return saleTime
     */
    public String getSaleSaleTime() {
        return saleTime;
    }

    /**
     * Gets sale total cost from sale
     * 
     * @return totalCost
     */
    public double getSaleTotalCost() {
        return totalCost;
    }

    /**
     * Gets sale total VAT from sale
     * 
     * @return totalVAT
     */
    public double getSaleTotalVAT() {
        return totalVAT;
    }

    /**
     * Gets paidAmount from sale instance
     * 
     * @return paidAmount
     */
    public double getSalePaidAmount() {
        return paidAmount;
    }

    /**
     * Gets change from sale instance
     * 
     * @return change
     */
    public double getSaleChange() {
        return change;
    }

    /**
     * Gets bought items, basket, from sale instance
     * 
     * @return basket
     */
    public List<ItemDTO> getSaleBasket() {
        return basket;
    }
}
