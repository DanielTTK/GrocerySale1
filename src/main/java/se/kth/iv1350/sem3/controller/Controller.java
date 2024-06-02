package se.kth.iv1350.sem3.controller;

import java.util.ArrayList;
import java.util.List;

import se.kth.iv1350.sem3.integration.ItemDTO;
import se.kth.iv1350.sem3.integration.ItemDoesNotExistException;
import se.kth.iv1350.sem3.integration.SystemDelegator;
import se.kth.iv1350.sem3.model.*;

/**
 * Only controller for this project. All calls to model pass through this class.
 */
public class Controller {
    private SystemDelegator delegator;
    private List<SaleObserver> saleObservers = new ArrayList<>();
    private Sale sale;
    private Reciept reciept;

    public Controller(SystemDelegator delegator) {
        this.delegator = delegator;
    }

    /**
     * Starts a new sale. This method must be called before doing anything else
     * during a sale. Ex. regItem.
     */
    public void startSale() {
        sale = new Sale(delegator);
        reciept = new Reciept(sale);
        // sale.notifyAllSaleObservers(saleObservers);
    }

    /**
     * Scans items and adds them to basket to later be proccessed for sale.
     * 
     * @param id       id of particular item to be scanned
     * @param quantity amount of same items scanned
     * @throws ItemDoesNotExistException
     */
    public void scanItem(String id, int quantity) throws ItemDoesNotExistException {
        sale.addItemToBasket(id, quantity);
    }

    /**
     * Gets basket for other classes, mainly view.
     * 
     * @return array of existing items ordered. If they do not exist they
     *         simply do not show
     */
    public List<ItemDTO> getBasket() {
        return sale.getBasket();
    }

    /**
     * Calcs total vat and cost. Allows for "breakpoints". You write as parameter
     * how many items you wanna go through
     * 
     * @param iteration how many lines within basket it should go through.
     */
    public void calcTotal(int iteration) {
        sale.calcTotal(iteration);
    }

    /**
     * Processes and "buys" items in basket, also removes them from inventory.
     * Always to be done last in a sale, should be executed after payment, as a
     * finalization of sale. Called finishSale in the model.
     * 
     * @param paidAmount
     * @throws ItemDoesNotExistException
     */
    public void pay(Amount paidAmount) throws ItemDoesNotExistException {
        sale.finishSale(paidAmount);
    }

    /**
     * Prints a reciept containing all information needed to prove that a sale has
     * taken place. Details the sale and accounting aswell.
     */
    public String createReciept() {
        return reciept.createRecieptDigital();
    }

    /**
     * Saves all details of a sale instance into a fake accounting database.
     */
    public void logSaleInAccounting() {
        sale.logSaleInAccounting();
    }

    /**
     * Gets the total cost from Sale inside model.
     * 
     * @return totalCost
     */
    public Amount getTotalCost() {
        return sale.getTotalCost();
    }

    /**
     * Gets the total VAT from Sale inside model.
     * 
     * @return totalVAT
     */
    public Amount getTotalVAT() {
        return sale.getTotalVAT();
    }

    /**
     * Gets the paid amount from Sale inside model.
     * 
     * @return paidAmount
     */
    public Amount getPaidAmount() {
        return sale.getPaidAmount();
    }

    /**
     * Gets calculated change from Sale inside model
     * 
     * @return change
     */
    public Amount getChange() {
        return sale.getChange();
    }

    /**
     * made for view to be able to view a method from Sale inside model.
     * 
     * @return saleTime
     */
    public String getSaleTime() {
        return sale.getSaleTime();
    }

    /**
     * Notifies this specific observer, param
     * 
     * @param observer
     */
    public void notifySpecificObserver(SaleObserver observer) {
        saleObservers.add(observer);
    }
}
