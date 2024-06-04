package se.kth.iv1350.sem3.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import se.kth.iv1350.sem3.integration.ItemDTO;
import se.kth.iv1350.sem3.integration.DoesNotExistException;
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
    public void testDetailsOfItemsAddedToBasket() throws DoesNotExistException {
        sale.addItemToBasket("abc123", 2);
        sale.addItemToBasket("def456", 1);

        ItemDTO item1 = sale.getBasket().get(0);
        ItemDTO item2 = sale.getBasket().get(1);

        assertTrue(item1.getQuantity() == 2 && item1.getCost().equals(new Amount(29.9 * 2).mathRound()),
                "Item1 could not be or has unexpected cost or quantity detailed! Cost: " + item1.getCost() + " Quan. = "
                        + item1.getQuantity());
        assertTrue(item2.getQuantity() == 1 && item2.getCost().equals(new Amount(14.9 * 1).mathRound()),
                "Item2 could not be or has unexpected cost or quantity detailed! Cost: " + item2.getCost() + " Quan. = "
                        + item2.getQuantity());
    }

    @Test
    public void testAddMultipleItemsWithQuantityOneToBasket() throws DoesNotExistException {
        sale.addItemToBasket("abc123", 1);
        sale.addItemToBasket("def456", 1);

        assertTrue(sale.getBasket().size() == 2,
                "Basket size is unreasonably large/small! Size: " + sale.getBasket().size());
    }

    @Test
    public void testAddOneItemWithQuantityMultipleToBasket() throws DoesNotExistException {
        sale.addItemToBasket("abc123", 2);

        ItemDTO item1 = sale.getBasket().get(0);

        assertTrue(item1.getQuantity() == 2 && item1.getCost().equals(new Amount(29.9 * 2).mathRound()),
                "Item1 with quantity 2 could not be added to basket! Cost: " + item1.getCost() + " Quan. = "
                        + item1.getQuantity());
    }

    @Test
    public void testAddMultipleItemsWithQuantityMultipleToBasket() throws DoesNotExistException {
        sale.addItemToBasket("abc123", 2);
        sale.addItemToBasket("def456", 2);

        assertTrue(sale.getBasket().size() == 2,
                "Basket size is too large or too small, it is supposed to be exactly 2, because there are two kinds of items added "
                        + sale.getBasket().size());
    }

    @Test
    public void testAddMultipleExistingItemToIncreaseQuantityInBasket() throws DoesNotExistException {
        sale.addItemToBasket("abc123", 2);
        sale.addItemToBasket("abc123", 1);

        ItemDTO item1 = sale.getBasket().get(0);

        assertTrue(item1.getQuantity() == 3 && item1.getCost().equals(new Amount(29.9 * 3).mathRound()),
                "Item1 quantity could not be increased inside basket! Item Cost: " + item1.getCost() + " Item Quan. = "
                        + item1.getQuantity());
    }

    @Test
    public void testCalcTotalOneItemMultipleQuantity() throws DoesNotExistException {
        sale.addItemToBasket("abc123", 2);

        sale.calcTotal(1);
        Amount total = sale.getTotalCost();
        Amount abc123cost = new Amount(29.90 * 1.06 * 2);

        assertTrue((total.equals(abc123cost)), "Failed to calc total. Total expected: " + total);
    }

    @Test
    public void testCalcTotalTwoDifferentItems() throws DoesNotExistException {
        sale.addItemToBasket("abc123", 2);
        sale.addItemToBasket("def456", 1);

        sale.calcTotal(2);
        Amount total = sale.getTotalCost();
        Amount abc123cost = new Amount(((29.90 * 2) + (14.90 * 1)) * 1.06);

        assertTrue((total.equals(abc123cost)), "Failed to calculate second item. Total expected: " + total);
    }

    @Test
    public void testCalcTotalSkipSecondItem() throws DoesNotExistException {
        sale.addItemToBasket("abc123", 2);
        sale.addItemToBasket("def456", 1);

        sale.calcTotal(1);
        Amount total = sale.getTotalCost();
        Amount abc123cost = new Amount((29.90 * 2) * 1.06);

        assertTrue((total.equals(abc123cost)),
                "Failed to calculate only first item. Total expected: " + total);
    }

    @Test
    public void testFinishSaleChangeAmount() throws DoesNotExistException {
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
    }

    @Test
    public void testFinishSaleQuantityDecreaseForItemInInventory() throws DoesNotExistException {
        sale.addItemToBasket("abc123", 1);
        sale.addItemToBasket("def456", 1);

        sale.calcTotal(2);

        sale.finishSale(new Amount(200));
        ItemDTO item1 = itemRegistry.returnItem("abc123");

        assertTrue(item1.getQuantity() == 1, "Item1 quantity did not get decreased! " + item1.getQuantity());
    }

    @Test
    public void testFinishSaleItemRemovalInInventoryDueToQuantity() throws DoesNotExistException {
        sale.addItemToBasket("abc123", 1);
        sale.addItemToBasket("def456", 1);

        sale.calcTotal(2);

        sale.finishSale(new Amount(200));

        assertTrue(itemRegistry.getInventoryArrayList().size() < 2,
                "Item2 did not get removed due to quantity! Length: " +
                        itemRegistry.getInventoryArrayList().size());
    }

    @Test
    public void testLogSaleInAccountingStorage() throws DoesNotExistException {
        Amount paidAmount = new Amount(29.9 * (1 + 0.06)); // 1 + vat multiplier, 6%, 0.06
        sale.addItemToBasket("abc123", 1);
        sale.calcTotal(1);
        sale.finishSale(paidAmount);
        sale.logSaleInAccounting();
        SaleDTO object = saleRegistry.getSaleRegistryList().get(0);

        assertTrue(
                object.getSaleBasket().size() == 1,
                "Sale not properly stored.");
    }

    @Test
    public void testLogSaleInAccountingTotalCost() throws DoesNotExistException {
        Amount paidAmount = new Amount(29.9 * (1 + 0.06)); // 1 + vat multiplier, 6%, 0.06
        sale.addItemToBasket("abc123", 1);
        sale.calcTotal(1);
        sale.finishSale(paidAmount);
        sale.logSaleInAccounting();
        SaleDTO object = saleRegistry.getSaleRegistryList().get(0);

        assertTrue(object.getSaleTotalCost().equals(new Amount(29.9).addAmt(object
                .getSaleTotalVAT())), "Wrong total cost " + object.getSaleTotalCost());

    }

    @Test
    public void testLogSaleInAccountingChange() throws DoesNotExistException {
        Amount paidAmount = new Amount(29.9 * (1 + 0.06)); // 1 + vat multiplier, 6%, 0.06
        sale.addItemToBasket("abc123", 1);
        sale.calcTotal(1);
        sale.finishSale(paidAmount);
        sale.logSaleInAccounting();
        SaleDTO object = saleRegistry.getSaleRegistryList().get(0);

        assertTrue(object.getSaleChange().equals(new Amount(0)), "Wrong change" + object.getSaleChange());
    }

    @Test
    public void testRemoveBoughtItemsFromRegistry() throws DoesNotExistException {
        // Private class ignored! Tested in testFinishSale method above.
        // Implemented in FinishSale method.
    }
}
