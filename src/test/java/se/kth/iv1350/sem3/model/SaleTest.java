package se.kth.iv1350.sem3.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import se.kth.iv1350.sem3.integration.ItemDTO;
import se.kth.iv1350.sem3.integration.ItemDoesNotExistException;
import se.kth.iv1350.sem3.integration.ItemRegistry;
import se.kth.iv1350.sem3.integration.SystemDelegator;
import se.kth.iv1350.sem3.util.Logger;

//import java.util.ArrayList;
import java.util.List;

public class SaleTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;
    private Sale sale;
    private Logger logger;
    private SystemDelegator delegator;
    private ItemRegistry itemRegistry;
    // private SaleRegistry saleRegistry;

    @BeforeEach
    public void renewSetUp() throws IOException {
        delegator = new SystemDelegator();
        itemRegistry = delegator.getItemRegistry();
        sale = new Sale(delegator);
        logger = new Logger();
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
    }

    @AfterEach
    public void tearDown() {
        delegator = null;
        sale = null;
        logger = null;
        printoutBuffer = null;
        System.setOut(originalSysOut);
    }

    @Test
    public void testAddItemToBasket() throws ItemDoesNotExistException {
        sale.addItemToBasket("abc123", 1);
        // sale.addItemToBasket("abc123", 1);

        List<ItemDTO> basket = sale.getBasket();
        String string = basket.toString();

        logger.logMessage(basket.get(0).toString());
        logger.logMessage(string);

        assertTrue(sale.getBasket().get(0).getQuantity() == 1, "Item could not be added to basket!");
    }

    @Test
    public void testCalcTotal() throws ItemDoesNotExistException {
        sale.addItemToBasket("abc123", 2);
        sale.addItemToBasket("def456", 1);

        sale.calcTotal(1);
        double total = sale.getTotalCost();
        double abc123cost = 29.90 * 1.06;

        assertTrue((total == abc123cost), "Failed to calc total. Total expected: " + total);
    }

    @Test
    public void testFinishSale() throws ItemDoesNotExistException {
        sale.addItemToBasket("abc123", 1);
        sale.addItemToBasket("abc123", 1);
        sale.addItemToBasket("def456", 1);

        sale.calcTotal(2);

        ItemDTO item1 = itemRegistry.returnItem("abc123");
        ItemDTO item2 = itemRegistry.returnItem("def456");

        double totalCost = (item1.getCost() * 2) + item2.getCost();
        double expectedChange = 200 - totalCost;

        sale.finishSale(200);

        assertTrue(sale.getChange() == expectedChange,
                "Incorrected change! Change expected: " + sale.getChange() + " Change found: "
                        + expectedChange);
        assertTrue(item1.getQuantity() == 1, "The item did not decrease!");
        assertTrue(itemRegistry.getInventory().length < 2, "The item did not decrease! Length: " + itemRegistry
                .getInventory().length);
    }

    @Test
    public void testLogSaleInAccounting() {
        // This test is not tested here, because there already is a test in
        // "SaleRegistry" class!
    }

    @Test
    public void testMathFloor() {
        double number = sale.mathFloor(100.111);

        assertTrue(number == 100.11, "The number did not get floored");
    }

    @Test
    public void testRemoveBoughtItemsFromRegistry() throws ItemDoesNotExistException {
        // Private class ignored! Tested in testFinishSale method above.
    }
}
