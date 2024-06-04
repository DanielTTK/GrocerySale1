package se.kth.iv1350.sem3.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import se.kth.iv1350.sem3.integration.ItemDTO;
import se.kth.iv1350.sem3.integration.DoesNotExistException;
import se.kth.iv1350.sem3.integration.ItemRegistry;
import se.kth.iv1350.sem3.integration.SaleRegistry;
import se.kth.iv1350.sem3.integration.SaleDTO;
import se.kth.iv1350.sem3.integration.SystemDelegator;

/**
 * a sale made by one customer.
 */
public class Sale {
    private ItemRegistry itemRegistry;
    private SaleRegistry saleRegistry;
    private LocalDateTime saleTime;

    private Amount totalCost;
    private Amount totalVAT;

    private Amount paidAmount;
    private Amount change;

    private List<SaleObserver> saleObservers = new ArrayList<>();
    private List<ItemDTO> basket = new ArrayList<>();

    /**
     * Creates a new instance and saves current time of sale.
     */
    public Sale(SystemDelegator delegator) {
        itemRegistry = delegator.getItemRegistry();
        saleRegistry = delegator.getSaleRegistry();
        saleTime = LocalDateTime.now();
    }

    /**
     * Adds item to be proccessed for sale into basket. Scan item function, based on
     * identifier and eventually inputted quantity.
     * 
     * @param id
     * @param quantity
     */
    public void addItemToBasket(String id, int quantity) throws DoesNotExistException { // Decide if exception
                                                                                        // should keep propegating
                                                                                        // upwards or if you want to
                                                                                        // catch it here. Why?
        if (basket.size() == 0) {
            basket.add(itemSetQuantity(itemRegistry.returnItem(id), quantity));
            return;
        }

        for (int i = 0; i < basket.size(); i++) {
            ItemDTO itemInstance = basket.get(i);
            if (id == itemInstance.getID()) {
                basket.set(i, itemAddedQuantity(itemInstance, quantity));
                return;
            }
        }

        basket.add(itemSetQuantity(itemRegistry.returnItem(id), quantity));
    }

    /**
     * Adds <code>quantity</code> to itemDTO quantity value.
     * 
     * @param item     item to add to
     * @param quantity amount of items to add
     * @return a copy of <code>item</code> but with <code>quantity</code> added
     *         quantity
     */
    private ItemDTO itemAddedQuantity(ItemDTO item, int quantity) throws DoesNotExistException { // should be in
                                                                                                 // itemRegistry
                                                                                                 // class?
        int resultingQuantity = (item.getQuantity() + quantity);
        Amount itemCostToAdd = (itemRegistry.returnItem(item.getID()).getCost().multiplyAmt(quantity));

        ItemDTO newItem = new ItemDTO(item.getID(), item.getName(), item.getDescription(),
                resultingQuantity, (item.getCost().addAmt(itemCostToAdd)).mathRound(), item.getVAT());
        return newItem;
    }

    /**
     * Sets <code>quantity</code> to itemDTO quantity value.
     * 
     * @param item     item to add to
     * @param quantity amount of items to add
     * @return a copy of <code>item</code> but with <code>quantity</code> added
     *         quantity
     */
    private ItemDTO itemSetQuantity(ItemDTO item, int quantity) throws DoesNotExistException {
        Amount itemCostToAdd = (itemRegistry.returnItem(item.getID()).getCost().multiplyAmt((quantity - 1)));

        ItemDTO newItem = new ItemDTO(item.getID(), item.getName(), item.getDescription(),
                quantity, (item.getCost().addAmt(itemCostToAdd)).mathRound(), item.getVAT());
        return newItem;
    }

    /**
     * Calculates total vat and cost. Allows for "breakpoints". You write as
     * parameter how many items you go through.
     * 
     * @param numberOfItemsToCalc How many items the method should iterate through
     *                            to calculate total vat and amount
     */
    public void calcTotal(int numberOfItemsToCalc) {
        totalCost = new Amount(0);
        List<ItemDTO> currBasket = getBasket();
        for (int i = 0; i < numberOfItemsToCalc; i++) {
            ItemDTO itemInstance = currBasket.get(i);
            totalCost = (totalCost.addAmt(itemInstance.getCost()));
        }
        totalVAT = totalCost.multiplyAmt(0.06);
        totalCost = totalCost.addAmt(totalVAT);
    }

    /**
     * Processes and "buys" items in basket, also removes them from inventory.
     * Always to be done last in a sale,
     * cannot remove items after this for example.
     * 
     * @throws DoesNotExistException
     * @returns change, amount the customer gets back.
     */
    public void finishSale(Amount paidAmount) throws DoesNotExistException {
        List<ItemDTO> currBasket = getBasket();
        this.paidAmount = paidAmount;

        change = paidAmount.subtractAmt(getTotalCost());
        removeBoughtItemsFromRegistry(currBasket);
    }

    /**
     * Logs details about sale instance in relevant places like databases.
     */
    public void logSaleInAccounting() {
        SaleDTO saleInstance = new SaleDTO(getSaleTime(), getBasket(), getTotalCost(),
                getTotalVAT(), getPaidAmount(), getChange());
        saleRegistry.saveSale(saleInstance);
        notifyObservers(saleInstance);
    }

    /**
     * Means to notifiy observers of a sale happening.
     * 
     * @param saleInstance
     */
    private void notifyObservers(SaleDTO saleInstance) {
        for (SaleObserver obs : saleObservers) {
            obs.finishedSale(saleInstance);
        }
    }

    /**
     * Removes items that exist in basket array from database
     * 
     * @param currentBasket basket array, consists of items to be bought
     * @throws DoesNotExistException
     */
    private void removeBoughtItemsFromRegistry(List<ItemDTO> currentBasket) throws DoesNotExistException {
        for (int i = 0; i < currentBasket.size(); i++) {
            ItemDTO itemInstance = currentBasket.get(i);
            ItemDTO itemToSubtract = itemRegistry.returnItem(itemInstance.getID());
            if (itemInstance.getQuantity() > itemToSubtract.getQuantity()) {
                // Not enough items in inventory exception
            } else if (itemToSubtract.getQuantity() - itemInstance.getQuantity() > 0) {
                itemRegistry.decreaseItemQuanityFromRegistry(itemInstance.getID(), itemInstance.getQuantity());
            } else {
                itemRegistry.removeItemFromRegistry(currentBasket.get(i).getID());
            }
        }
    }

    /**
     * Gets basket for other classes..
     * 
     * @return array of existing items ordered, mainly view.
     */
    public List<ItemDTO> getBasket() {
        return basket;
    }

    /**
     * Gets total cost for other classes, mainly view.
     * 
     * @return totalCost
     */
    public Amount getTotalCost() {
        return totalCost;
    }

    /**
     * Gets total vat for other classes, mainly view.
     * 
     * @return totalVAT
     */
    public Amount getTotalVAT() {
        return totalVAT;
    }

    /**
     * Gets the paid amount by customer for other classes, mainly view.
     * 
     * @return paid amount by customer
     */
    public Amount getPaidAmount() {
        return paidAmount;
    }

    /**
     * Gets change for other classes, mainly view.
     * 
     * @return array of existing items ordered
     */
    public Amount getChange() {
        return change;
    }

    /**
     * Gets sale time and formats it to a more user friendly way. Reference is
     * javase - docs.oracle.com.
     * 
     * @return formatted, current time taken from machine.
     */
    public String getSaleTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return formatter.format(saleTime);
    }

    /**
     * Adds a sale observers
     * 
     * @param obs
     */
    public void addSaleObserver(SaleObserver obs) {
        saleObservers.add(obs);
    }

    /**
     * Notifies all the observers by reregistring them when they need to be
     * notified.
     * 
     * @param observers
     */
    public void addAllSaleObservers(List<SaleObserver> observers) {
        saleObservers.addAll(observers);
    }

}
