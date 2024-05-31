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
import se.kth.iv1350.sem3.integration.SystemDelegator;
import se.kth.iv1350.sem3.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class SaleTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;
    private Sale sale;
    private Logger logger;
    private SystemDelegator delegator;

    @BeforeEach
    public void renewSetUp() throws IOException {
        delegator = new SystemDelegator();
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
    public void testCalcTotal() {
        sale.calcTotal(3);
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
        // Private class ignored!
    }
}
