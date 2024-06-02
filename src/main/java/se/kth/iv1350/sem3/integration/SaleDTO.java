package se.kth.iv1350.sem3.integration;

import java.util.List;

import se.kth.iv1350.sem3.model.Amount;

/**
 * An object, DTO, that stores sale details.
 */
public class SaleDTO {
    private final String saleTime;
    private final Amount totalCost;
    private final Amount totalVAT;
    private final Amount paidAmount;
    private final Amount change;
    private final List<ItemDTO> basket;

    /**
     * Constructs/creates an object representing a Sale occurance. DTO.
     * 
     * @param saleTime
     * @param basket
     * @param totalCost
     * @param totalVAT
     * @param paidAmount
     * @param change
     */
    public SaleDTO(String saleTime, List<ItemDTO> basket,
            Amount totalCost, Amount totalVAT, Amount paidAmount, Amount change) {
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
    public Amount getSaleTotalCost() {
        return totalCost;
    }

    /**
     * Gets sale total VAT from sale
     * 
     * @return totalVAT
     */
    public Amount getSaleTotalVAT() {
        return totalVAT;
    }

    /**
     * Gets <code>paidAmount</code> from sale instance
     * 
     * @return paidAmount
     */
    public Amount getSalePaidAmount() {
        return paidAmount;
    }

    /**
     * Gets <code>change</code> from sale instance
     * 
     * @return change
     */
    public Amount getSaleChange() {
        return change;
    }

    /**
     * Gets list of bought items, <code>basket</code>, from sale instance
     * 
     * @return basket
     */
    public List<ItemDTO> getSaleBasket() {
        return basket;
    }
}
