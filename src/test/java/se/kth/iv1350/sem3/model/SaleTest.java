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
import se.kth.iv1350.sem3.integration.SaleDTO;
import se.kth.iv1350.sem3.integration.SaleRegistry;
import se.kth.iv1350.sem3.integration.SystemDelegator;

public class SaleTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;
    private Sale sale;
    // private Logger logger;
    private SystemDelegator delegator;
    private ItemRegistry itemRegistry;
    private SaleRegistry saleRegistry;

    @BeforeEach
    public void renewSetUp() throws IOException {
        delegator = new SystemDelegator();
        itemRegistry = delegator.getItemRegistry();
        saleRegistry = delegator.getSaleRegistry();
        sale = new Sale(delegator);
        // logger = new Logger();
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
    }

    @AfterEach
    public void tearDown() {
        delegator = null;
        sale = null;
        // logger = null;
        printoutBuffer = null;
        System.setOut(originalSysOut);
    }

    @Test
    public void testAddItemToBasket() throws ItemDoesNotExistException {
        sale.addItemToBasket("abc123", 2);
        sale.addItemToBasket("abc123", 1);
        sale.addItemToBasket("def456", 1);
        sale.addItemToBasket("def456", 2);

        ItemDTO item1 = sale.getBasket().get(0);
        ItemDTO item2 = sale.getBasket().get(1);

        // logger.logMessage(basket.get(0).toString());
        // logger.logMessage(string);

        assertTrue(item1.getQuantity() == 3 && item1.getCost() == sale.mathRound(29.9 * 3),
                "Item1 could not be added to basket! Cost: " + item1.getCost() + " Quan. = " + item1.getQuantity());
        assertTrue(item2.getQuantity() == 3 && item2.getCost() == sale.mathRound(14.9 * 3),
                "Item2 could not be added to basket! Cost: " + item2.getCost() + " Quan. = " + item2.getQuantity());
    }

    @Test
    public void testCalcTotal() throws ItemDoesNotExistException {
        sale.addItemToBasket("abc123", 2);
        sale.addItemToBasket("def456", 1);

        sale.calcTotal(1);
        double total = sale.getTotalCost();
        double abc123cost = 29.90 * 1.06 * 2;

        assertTrue((total == abc123cost), "Failed to calc total. Total expected: " + total);
    }

    @Test
    public void testFinishSale() throws ItemDoesNotExistException {
        sale.addItemToBasket("abc123", 1);
        sale.addItemToBasket("def456", 1);

        sale.calcTotal(2);

        ItemDTO item1 = itemRegistry.returnItem("abc123");
        ItemDTO item2 = itemRegistry.returnItem("def456");

        double totalCost = item1.getCost() + item2.getCost();
        double expectedChange = 200 - totalCost;

        sale.finishSale(200);

        assertTrue(sale.getChange() == expectedChange,
                "Incorrected change! Change expected: " + sale.getChange() + " Change found: "
                        + expectedChange);
        assertTrue(item1.getQuantity() == 1, "The item did not decrease!");
        assertTrue(itemRegistry.getInventoryArrayList().size() < 2, "The item did not decrease! Length: " +
                itemRegistry.getInventoryArrayList().size());
    }

    @Test
    public void testLogSaleInAccounting() throws ItemDoesNotExistException {
        sale.addItemToBasket("abc123", 1);
        sale.calcTotal(1);
        sale.finishSale(29.9);
        sale.logSaleInAccounting();
        SaleDTO object = saleRegistry.getSaleRegistry().get(0);

        assertTrue(
                object.getSaleBasket().size() == 1,
                "Sale not properly stored.");
        assertTrue(object.getSaleTotalCost() == 29.9 + object.getSaleTotalVAT(), "Wrong total cost " +
                object.getSaleTotalCost());
        assertTrue(object.getSaleChange() == 0, "Wrong change" + object.getSaleChange());
    }

    @Test
    public void testMathFloor() {
        double number = sale.mathFloor(100.111);

        assertTrue(number == 100.11, "The number did not get floored");
    }

    @Test
    public void testMathRound() {
        double number = sale.mathRound(100.111);

        assertTrue(number == 100.1, "The number did not get rounded");
    }

    @Test
    public void testRemoveBoughtItemsFromRegistry() throws ItemDoesNotExistException {
        // Private class ignored! Tested in testFinishSale method above.
    }
}
