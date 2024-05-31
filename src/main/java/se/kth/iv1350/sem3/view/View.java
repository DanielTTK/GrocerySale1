/*
 * General thing to work on:
 * 
 * Deepen understanding on,
 *  definitions in constructors
 *  
 */

package se.kth.iv1350.sem3.view;

import java.io.IOException;
//import java.util.ArrayList;
import java.util.List;

import se.kth.iv1350.sem3.controller.Controller;
//import se.kth.iv1350.sem3.util.Logger;
import se.kth.iv1350.sem3.integration.ItemDTO;
import se.kth.iv1350.sem3.integration.ItemDoesNotExistException;

/**
 * This is a placeholder for the real view. Contains hardcoded execution,
 * calling methods inside controller.
 */
public class View {
    private Controller contr;

    /**
     * Creates new instance.
     * 
     * @param contr The controller that is used for all operations.
     * @throws IOException if logger fails.
     */
    public View(Controller contr) throws IOException { // good example of constructor that doesnt need test
        this.contr = contr;
    }

    /**
     * Simulating a user input from a view screen that calls to all system
     * operations. Prints out everything that happens in groups. For example, the
     * pay method call both acknowledges the payment the customer makes but also
     * removes items from the item inventory, therefor they are printed close to
     * eachother.
     * 
     * @throws DoesNotExistException exception if scanned item does not exist.
     */
    public void runFakeExecution() throws ItemDoesNotExistException {
        contr.startSale();
        System.out.println("Sale has been started.");

        contr.scanItem("abc123", 1);
        contr.scanItem("abc123", 1);
        contr.scanItem("def456", 1);
        displayAllScannedItemsFromBasket();

        contr.pay(200);
        System.out.println("Customer pays " + contr.mathFloor(contr.getPaidAmount()) + " SEK");
        System.out.println("Basket items successfully decreased from inventory.");

        contr.logSaleInAccounting();
        System.out.println("\nSale succesfully logged into accounting system.");

        System.out.print(contr.createReciept());
    }

    /**
     * Displays all scanned items, better said all items currently within the
     * basket as output to system.
     */
    public void displayAllScannedItemsFromBasket() {
        List<ItemDTO> currBasket = contr.getBasket();

        for (int i = 0; i < currBasket.size(); i++) {
            ItemDTO itemInstance = currBasket.get(i);
            contr.calcTotal(i + 1);
            System.out.print("Add " + itemInstance.getQuantity() + " item with identifier " + itemInstance.getID() +
                    "\nItem ID: " + itemInstance.getID() +
                    "\nItem name: " + itemInstance.getName() + " x" + itemInstance.getQuantity() +
                    "\nItem(s) cost: " + itemInstance.getCost() +
                    "\nVAT: " + itemInstance.getVAT() * 100 + "%" +
                    "\nItem description: " + itemInstance.getDescription() +

                    "\n\nTotal cost (incl VAT): " + contr.mathFloor(contr.getTotalCost()) + " SEK" +
                    "\nTotal VAT: " + contr.mathFloor(contr.getTotalVAT()) + "\n\n");
        }
    }
}
