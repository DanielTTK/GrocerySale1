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
    private SystemDelegator delegator;
    private ItemRegistry itemRegistry;
    private SaleRegistry saleRegistry;

    @BeforeEach
    public void renewSetUp() throws IOException {
        delegator = new SystemDelegator();
        itemRegistry = delegator.getItemRegistry();
        saleRegistry = delegator.getSaleRegistry();
        sale = new Sale(delegator);
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
    }

    @AfterEach
    public void tearDown() {
        delegator = null;
        sale = null;
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

        assertTrue(item1.getQuantity() == 3 && item1.getCost().equals(new Amount(29.9 * 3).mathRound()),
                "Item1 could not be added to basket! Cost: " + item1.getCost() + " Quan. = "
                        + item1.getQuantity());
        assertTrue(item2.getQuantity() == 3 && item2.getCost().equals(new Amount(14.9 * 3).mathRound()),
                "Item2 could not be added to basket! Cost: " + item2.getCost() + " Quan. = "
                        + item2.getQuantity());
    }

    @Test
    public void testCalcTotal() throws ItemDoesNotExistException {
        sale.addItemToBasket("abc123", 2);
        sale.addItemToBasket("def456", 1);

        sale.calcTotal(1);
        Amount total = sale.getTotalCost();
        Amount abc123cost = new Amount(29.90 * 1.06 * 2);

        assertTrue((total.equals(abc123cost)), "Failed to calc total. Total expected: " + total.toString());
    }

    @Test
    public void testFinishSale() throws ItemDoesNotExistException {
        sale.addItemToBasket("abc123", 1);
        sale.addItemToBasket("def456", 1);

        sale.calcTotal(2);

        ItemDTO item1 = itemRegistry.returnItem("abc123");
        ItemDTO item2 = itemRegistry.returnItem("def456");

        Amount totalCost = (item1.getCost().addAmt(item2.getCost())).multiplyAmt(1.06);
        Amount expectedChange = new Amount(200).subtractAmt(totalCost);

        sale.finishSale(new Amount(200));

        assertTrue(sale.getChange().equals(expectedChange),
                "Incorrected change! Change expected: " + sale.getChange() + " Change found: "
                        + expectedChange);
        assertTrue(item1.getQuantity() == 1, "The item did not decrease!");
        assertTrue(itemRegistry.getInventoryArrayList().size() < 2, "The item did not decrease! Length: " +
                itemRegistry.getInventoryArrayList().size());
    }

    @Test
    public void testLogSaleInAccounting() throws ItemDoesNotExistException {
        Amount paidAmount = new Amount(29.9 * (1 + 0.06)); // 1 + vat multiplier, 6%, 0.06
        sale.addItemToBasket("abc123", 1);
        sale.calcTotal(1);
        sale.finishSale(paidAmount);
        sale.logSaleInAccounting();
        SaleDTO object = saleRegistry.getSaleRegistry().get(0);

        assertTrue(
                object.getSaleBasket().size() == 1,
                "Sale not properly stored.");
        assertTrue(object.getSaleTotalCost().equals(new Amount(29.9).addAmt(object
                .getSaleTotalVAT())), "Wrong total cost " + object.getSaleTotalCost());
        assertTrue(object.getSaleChange().equals(new Amount(0)), "Wrong change" + object.getSaleChange());
    }

    @Test
    public void testMathFloor() {
        Amount number = new Amount(100.111).mathFloor();

        assertTrue(number.equals(new Amount(100.11)), "The number did not get floored");
    }

    @Test
    public void testMathRound() {
        Amount number = (new Amount(100.111)).mathRound();

        assertTrue(number.equals(new Amount(100.1)), "The number did not get rounded");
    }

    @Test
    public void testRemoveBoughtItemsFromRegistry() throws ItemDoesNotExistException {
        // Private class ignored! Tested in testFinishSale method above.
        // Implemented in FinishSale method.
    }
}
