package se.kth.iv1350.sem3.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import se.kth.iv1350.sem3.integration.ItemDTO;
import se.kth.iv1350.sem3.integration.ItemDoesNotExistException;
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

    private double totalCost;
    private double totalVAT;

    private double paidAmount;
    private double change;

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
    public void addItemToBasket(String id, int quantity) throws ItemDoesNotExistException { // Decide if exception
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
    private ItemDTO itemAddedQuantity(ItemDTO item, int quantity) throws ItemDoesNotExistException { // should be in
                                                                                                     // itemRegistry
                                                                                                     // class?
        int resultingQuantity = (item.getQuantity() + quantity);
        double itemCostToAdd = (itemRegistry.returnItem(item.getID()).getCost() * quantity);

        ItemDTO newItem = new ItemDTO(item.getID(), item.getName(), item.getDescription(),
                resultingQuantity, mathRound(item.getCost() + itemCostToAdd), item.getVAT());
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
    private ItemDTO itemSetQuantity(ItemDTO item, int quantity) throws ItemDoesNotExistException {
        double itemCostToAdd = (itemRegistry.returnItem(item.getID()).getCost() * (quantity - 1));

        ItemDTO newItem = new ItemDTO(item.getID(), item.getName(), item.getDescription(),
                quantity, mathRound(item.getCost() + itemCostToAdd), item.getVAT());
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
        totalCost = 0;
        List<ItemDTO> currBasket = getBasket();
        for (int i = 0; i < numberOfItemsToCalc; i++) {
            ItemDTO itemInstance = currBasket.get(i);
            totalCost += itemInstance.getCost();
        }
        totalVAT = totalCost * 0.06;
        totalCost += totalVAT;
    }

    /**
     * Processes and "buys" items in basket, also removes them from inventory.
     * Always to be done last in a sale,
     * cannot remove items after this for example.
     * 
     * @throws ItemDoesNotExistException
     * @returns change, amount the customer gets back.
     *          --> Displays this to cashier. No additional functionality in that
     *          regard
     * 
     */
    public void finishSale(double paidAmount) throws ItemDoesNotExistException {
        List<ItemDTO> currBasket = getBasket();
        double cost = 0;

        this.paidAmount = paidAmount;

        for (int i = 0; i < currBasket.size(); i++) {
            cost += currBasket.get(i).getCost();
        }

        change = paidAmount - cost;
        removeBoughtItemsFromRegistry(currBasket);
    }

    /**
     * Logs details about sale instance in external database.
     */
    public void logSaleInAccounting() {
        SaleDTO saleInstance = new SaleDTO(getSaleTime(), getBasket(), getTotalCost(),
                getTotalVAT(), getPaidAmount(), getChange());
        saleRegistry.saveSale(saleInstance);
    }

    /**
     * Floors doubles with 2 significant decimals
     * 
     * @param number number to be floored
     * @return returns floored number
     */
    public double mathFloor(double number) {
        return Math.floor(number * 100) / 100;
    }

    /**
     * Rounds doubles with 1 significant decimal
     * 
     * @param number number to be floored
     * @return returns floored number
     */
    public double mathRound(double number) {
        return Math.round(number * 10.0) / 10.0;
    }

    /**
     * Removes items that exist in basket array from database
     * 
     * @param currentBasket basket array, consists of items to be bought
     * @throws ItemDoesNotExistException
     */
    private void removeBoughtItemsFromRegistry(List<ItemDTO> currentBasket) throws ItemDoesNotExistException {
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

    /*
     * private void notifyObservers(ItemDTO soldItem) {
     * for (SaleObserver obs : saleObservers) {
     * obs.finishedSale(soldItem);
     * }
     * }
     */
    public void notifySpecificSaleObserver(SaleObserver obs) { // add javadocs to these
        saleObservers.add(obs);
    }

    public void notifyAllSaleObservers(List<SaleObserver> observers) {
        saleObservers.addAll(observers);
    }

    /**
     * Gets basket for other classes, mainly view.
     * 
     * @return array of existing items ordered
     */
    public List<ItemDTO> getBasket() {
        return basket;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getTotalVAT() {
        return totalVAT;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public double getChange() {
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
}
