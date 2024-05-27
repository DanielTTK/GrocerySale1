package se.kth.iv1350.sem3.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import se.kth.iv1350.sem3.integration.ItemDTO;
import se.kth.iv1350.sem3.integration.ItemDoesNotExistException;
import se.kth.iv1350.sem3.integration.ItemRegistry;

/**
 * a sale made by one customer.
 */
public class Sale {
    private ItemRegistry itemRegistry;
    private LocalTime saleTime;

    private double totalCost;
    private double totalVAT;

    private double paidAmount;
    private double change;

    private List<SaleObserver> saleObservers = new ArrayList<>();
    private List<ItemDTO> basket = new ArrayList<>();

    /**
     * Creates a new instance and saves time of sale.
     */
    public Sale(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
        saleTime = LocalTime.now();
        // receipt = new Receipt();
    }

    /**
     * Adds item to be proccessed for sale. Execs this when scanning items
     * 
     * @param id
     * @param quantity
     */
    public void addItemToBasket(String id, int quantity) throws ItemDoesNotExistException { // Decide if exception
                                                                                            // should keep propegating
                                                                                            // upwards or if you want to
                                                                                            // catch it here. Why?
        boolean itemExists = false;
        for (int i = 0; i < basket.size(); i++) {
            ItemDTO itemInstance = basket.get(i);
            if (id == itemInstance.getID()) {
                basket.set(i, itemAddedQuantity(itemInstance, quantity));
                itemExists = true;
            }
        }
        if (!itemExists) {
            basket.add(itemAddedQuantity(itemRegistry.returnItem(id), quantity));
        }
    }

    /**
     * Adds amount to itemDTO quantity value. Changes item based on quantity
     * 
     * @param item     item to add to
     * @param quantity amount of items to add
     * @return a copy of <code>item</code> but with <code>quantity</code> added
     *         quantity
     */
    ItemDTO itemAddedQuantity(ItemDTO item, int quantity) {
        ItemDTO newItem = new ItemDTO(item.getID(), item.getName(), item.getDescription(),
                quantity, item.getCost() * quantity, item.getVAT());
        return newItem;
    }

    /**
     * Calculates total vat and amount which later can be gotten.
     * 
     * @param numberOfItemsToCalc How many items the method should iterate through
     *                            to calculate total vat and amount
     */
    public void calcTotal(int numberOfItemsToCalc) {
        totalCost = 0;
        totalVAT = 0;
        List<ItemDTO> currBasket = getBasket();
        for (int i = 0; i < numberOfItemsToCalc; i++) {
            ItemDTO itemInstance = currBasket.get(i);

            totalVAT += itemInstance.getCost() * itemInstance.getVAT();
            totalCost += itemInstance.getCost() + totalVAT;
        }
    }

    /**
     * Processes and "buys" items in basket. Always to be done last in a sale,
     * cannot remove items after this for example.
     * 
     * @throws ItemDoesNotExistException
     * @returns change, amount the customer gets back.
     *          --> Displays this to cashier. No additional functionality in that
     *          regard
     * 
     */
    public void finishSale(double paidAmount) throws ItemDoesNotExistException {
        String idOfBoughtItem = " ";
        List<ItemDTO> currBasket = getBasket();
        double cost = 0;

        this.paidAmount = paidAmount;

        for (int i = 0; i < currBasket.size(); i++) {
            cost += currBasket.get(i).getCost();
            itemRegistry.removeItemFromRegistry(idOfBoughtItem);
        }

        change = paidAmount - cost;
        removeBoughtItemsFromRegistry(currBasket);
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
     * Removes items that exist in basket array from database
     * 
     * @param currentBasket basket array, consists of items to be bought
     * @throws ItemDoesNotExistException
     */
    private void removeBoughtItemsFromRegistry(List<ItemDTO> currentBasket) throws ItemDoesNotExistException {
        for (int i = 0; i < currentBasket.size(); i++) {
            itemRegistry.removeItemFromRegistry(currentBasket.get(i).getID());
        }
    }

    private void notifyObservers(ItemDTO soldItem) {
        for (SaleObserver obs : saleObservers) {
            obs.finishedSale(soldItem);
        }
    }

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

    public LocalTime getSaleTime() {
        return saleTime;
    }
}
