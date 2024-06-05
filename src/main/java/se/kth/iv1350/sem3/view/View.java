package se.kth.iv1350.sem3.view;

import java.io.IOException;
import java.util.List;

import se.kth.iv1350.sem3.controller.Controller;
import se.kth.iv1350.sem3.controller.GeneralException;
import se.kth.iv1350.sem3.integration.ItemDTO;
import se.kth.iv1350.sem3.integration.DoesNotExistException;
import se.kth.iv1350.sem3.model.Amount;
import se.kth.iv1350.sem3.util.Logger;

/**
 * This is a placeholder for the real view. Contains hardcoded execution,
 * calling methods inside controller.
 */
public class View {
    private Controller contr;
    private Logger logger;
    private ErrorDisplay errorDisplay = new ErrorDisplay();

    private RandomImplementation randomImplementation = new RandomImplementation();
    private RandomComposition randomComposition = new RandomComposition();

    /**
     * Creates new instance.
     * 
     * @param contr The controller that is used for all operations.
     * @throws IOException if logger fails.
     */
    public View(Controller contr) throws IOException { // good example of constructor that doesnt need test
        this.contr = contr;
        contr.addSaleObserver(new TotalRevenueView());
        contr.addSaleObserver(new TotalRevenueFileOutput());
        this.logger = new Logger();
    }

    /**
     * Simulating a user input from a view screen that calls to all system
     * operations.
     * 
     * Prints out everything that happens in groups. For example, the
     * pay method call both acknowledges the payment the customer makes but also
     * removes items from the item inventory, therefore they are printed close to
     * eachother.
     * 
     * @throws DoesNotExistException exception if scanned item does not exist.
     */
    public void runFakeExecution() {
        try {
            contr.startSale();
            System.out.println("Sale has been started.");

            contr.scanItem("abc123", 1);
            contr.scanItem("abc123", 1);
            contr.scanItem("def456", 1);
            displayAllScannedItemsFromBasket();

            contr.pay(new Amount(200));
            System.out.println("Customer pays " + (contr.getPaidAmount()).mathFloor() + " SEK");
            System.out.println("Basket items successfully decreased from inventory.");

            System.out.println("Initiating failure control, attempting to cause an error...\n");
            tryToFail();

            contr.logSaleInAccounting();
            System.out.println("\nSale succesfully logged into accounting system.");

            System.out.print(contr.createReciept());

        } catch (DoesNotExistException exception) {
            errorDisplay.writeErrorMessage("An item you attempted to order does NOT exist!");
        } catch (Exception exception) {
            writeToLogAndUI("Failed to scan items...", exception);
        }

        System.out.println("\n\nTesting if random implementation works, task 2...");
        System.out.println(randomImplementation.nextInt(50));

        System.out.println("\nTesting if random composition works, task 2...");
        System.out.println(randomComposition.nextInt(50));
    }

    /**
     * Code to deliberatly fail the program
     */
    private void tryToFail() {
        try {
            System.out.println("Scanning identifier that does not exist, error expected.");
            contr.scanItem("rnd123", 1);
            System.out.println("Managed to scan an item that does not exist!");
        } catch (DoesNotExistException exception) {
            errorDisplay.writeErrorMessage("Successful failure of scanning of unexisting item!");
        } catch (GeneralException wrongexc) {
            writeToLogAndUI("Unexpected exception, failed test", wrongexc);
        }

        try {
            System.out.println("Trying to induce a database error with the identifier 'err111'");
            contr.scanItem("err111", 1);
        } catch (GeneralException exception) {
            writeToLogAndUI("Expected exception thrown, program works.", exception);
        } catch (DoesNotExistException wrongexc) {
            errorDisplay.writeErrorMessage("Completely wrong exception thrown...");
        }
    }

    /**
     * Displays all scanned items, better said all items currently within the
     * basket as output to system.
     */
    private void displayAllScannedItemsFromBasket() {
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

                    "\n\nTotal cost (incl VAT): " + (contr.getTotalCost()).mathFloor() + " SEK" +
                    "\nTotal VAT: " + (contr.getTotalVAT()).mathFloor() + "\n\n");
        }
    }

    /**
     * Writes to both dev logger and also to a view class.
     * 
     * @param displayMsg
     * @param exception
     */
    private void writeToLogAndUI(String displayMsg, Exception exception) {
        errorDisplay.writeErrorMessage(displayMsg);
        logger.logException(exception);
    }
}
