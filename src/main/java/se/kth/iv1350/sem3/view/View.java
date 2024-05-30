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
     * operations.
     * 
     * @throws DoesNotExistException exception if scanned item does not exist.
     */
    public void runFakeExecution() throws ItemDoesNotExistException {
        contr.startSale();

        contr.scanItem("abc123", 2); // finds item from contr-->integration and adds the count there
        contr.scanItem("def456", 1);

        contr.pay(200);
        addItems();
        printRecieptDigital();
    }

    /**
     * adds items and displays it on view for every item added.
     */
    public void addItems() {
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

        System.out.print("\n\nCustomer pays " + contr.mathFloor(contr.getPaidAmount()) + " SEK");
        System.out.print("\n\nBasket items successfully decreased from inventory.");
    }

    public void printRecieptDigital() { // this method should be in reciept
        List<ItemDTO> currBasket = contr.getBasket();
        System.out.println("\n------------------ Begin receipt ------------------");
        System.out.println("Time of Sale: " + contr.getSaleTime());
        for (int i = 0; i < currBasket.size(); i++) {
            ItemDTO itemInstance = currBasket.get(i);
            contr.calcTotal(i + 1);
            System.out.println(itemInstance.getName() + "             " +
                    itemInstance.getQuantity() + " x " + itemInstance.getCost() + "      " +
                    itemInstance.getCost() + " SEK");
        }
        System.out.println("\nTotal: " + contr.mathFloor(contr.getTotalCost()) + " SEK" +
                "\nVAT: " + contr.mathFloor(contr.getTotalVAT()) +

                "\n\nPayment: " + contr.mathFloor(contr.getPaidAmount()) +
                "\nChange: " + contr.mathFloor(contr.getChange()) +
                "\n------------------- End receipt -------------------" +
                "\n\nChange to give the customer: " + contr.mathFloor(contr.getChange()));
    }
}
