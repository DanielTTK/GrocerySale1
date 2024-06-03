package se.kth.iv1350.sem3.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.sem3.integration.ItemDTO;
import se.kth.iv1350.sem3.integration.ItemDoesNotExistException;
import se.kth.iv1350.sem3.integration.ItemRegistry;
import se.kth.iv1350.sem3.integration.SaleDTO;
import se.kth.iv1350.sem3.integration.SaleRegistry;
import se.kth.iv1350.sem3.integration.SystemDelegator;
import se.kth.iv1350.sem3.model.Amount;

public class ControllerTest {
    private Controller contr;
    private SystemDelegator delegator;
    ByteArrayOutputStream printoutBuffer;
    PrintStream originalSysOut;

    @BeforeEach
    public void setUp() throws IOException {
        delegator = new SystemDelegator();
        contr = new Controller(delegator);

        // Change out old out with new.
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);

        contr.startSale();
    }

    @AfterEach
    public void tearDown() {
        printoutBuffer = null;
        System.setOut(originalSysOut);
        delegator = null;
        contr = null;
    }

    @Test
    public void testIdentifierScanItem() throws ItemDoesNotExistException {
        contr.scanItem("abc123", 1);
        String expectedResult = "abc123";
        String result = contr.getBasket().get(0).getID();
        assertTrue(expectedResult == result, "Item not scanned/added to basket properly.");
    }

    @Test
    public void testQuantityScanItem() throws ItemDoesNotExistException {
        contr.scanItem("abc123", 1);
        int expectedResult = 1;
        int result = contr.getBasket().get(0).getQuantity();
        assertTrue(expectedResult == result, "Item not scanned/added to basket properly.");
    }

    @Test
    public void testCalcTotal() throws ItemDoesNotExistException {
        contr.scanItem("abc123", 1);
        contr.calcTotal(1);
        Amount basketTotalCost = contr.getTotalCost();
        Amount expectedTotalCost = new Amount(29.9 * 1.06);

        assertTrue(basketTotalCost.equals(expectedTotalCost),
                "invalid cost, the item is not equal to expected cost.");
    }

    @Test
    public void testPayChange() throws ItemDoesNotExistException {
        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        Amount expectedChange = contr.getPaidAmount().subtractAmt(contr.getTotalCost());
        Amount change = contr.getChange();

        assertTrue(expectedChange.equals(change), "Unexpected change amount, it did not get calculated properly.");
    }

    @Test
    public void testPayInventoryItemQuantityDecrease() throws ItemDoesNotExistException {
        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        ItemRegistry inventory = delegator.getItemRegistry();
        int quantity = inventory.returnItem("abc123").getQuantity();
        int expectedQuantity = 1;

        assertTrue(quantity == expectedQuantity, "item did not decrease quantity from inventory properly!");
    }

    @Test
    public void testPayInventoryItemRemovalByQuantity() throws ItemDoesNotExistException {
        contr.scanItem("def456", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        ItemRegistry inventory = delegator.getItemRegistry();
        int inventorySize = inventory.getInventoryArrayList().size();
        int expectedSize = 1;

        assertTrue(inventorySize == expectedSize, "item did not get removed!");
    }

    @Test
    public void testCreateRecieptChange() throws ItemDoesNotExistException {
        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        String recieptString = contr.createReciept();
        String expectedChangeInStringForm = contr.getPaidAmount().subtractAmt(contr.getTotalCost()).mathFloor()
                .toString();

        assertTrue(recieptString.contains(expectedChangeInStringForm), "Invalid change in reciept!");
    }

    @Test
    public void testCreateRecieptItemCost() throws ItemDoesNotExistException {
        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        ItemDTO item = contr.getBasket().get(0);

        String recieptString = contr.createReciept();
        String expectedItemCostString = item.getCost().toString();

        assertTrue(recieptString.contains(expectedItemCostString), "Invalid item cost in reciept!");
    }

    @Test
    public void testCreateRecieptTotalCost() throws ItemDoesNotExistException {
        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        String recieptString = contr.createReciept();
        String expectedTotalCostString = contr.getTotalCost().mathFloor().toString();

        assertTrue(recieptString.contains(expectedTotalCostString), "Invalid total cost in reciept!");

    }

    @Test
    public void testCreateRecieptTotalVAT() throws ItemDoesNotExistException {
        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        String recieptString = contr.createReciept();
        String expectedTotalVATString = contr.getTotalVAT().mathFloor().toString();

        assertTrue(recieptString.contains(expectedTotalVATString), "Invalid total vat in reciept!");

    }

    @Test
    public void testCreateRecieptPaidAmount() throws ItemDoesNotExistException {
        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));
        String recieptString = contr.createReciept();

        String expectedPaidAmountString = contr.getPaidAmount().toString();

        assertTrue(recieptString.contains(expectedPaidAmountString), "Invalid paid amount in reciept!");
    }

    @Test
    public void testCreateRecieptQuantity() throws ItemDoesNotExistException {
        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        String recieptString = contr.createReciept();

        String expectedQuantityString = "1";

        assertTrue(recieptString.contains(expectedQuantityString), "Invalid quantity in reciept!");
    }

    @Test
    public void testLogSaleInAccountingListSize() throws ItemDoesNotExistException {
        SaleRegistry accounting = delegator.getSaleRegistry();

        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        contr.logSaleInAccounting();

        int outcome = accounting.getSaleRegistryList().size();
        int expectedSaleListSize = 1;

        assertTrue(expectedSaleListSize == outcome, "Nothing was added to the saleRegistry/accounting");
    }

    @Test
    public void testLogSaleInAccountingSaleTime() throws ItemDoesNotExistException {
        SaleRegistry accounting = delegator.getSaleRegistry();

        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        contr.logSaleInAccounting();

        SaleDTO expectedStoredSaleDTO = new SaleDTO(contr.getSaleTime(), contr.getBasket(),
                contr.getTotalCost().mathFloor(),
                contr.getTotalVAT(), contr.getPaidAmount(), contr.getChange().mathFloor());

        SaleDTO storedSaleDTO = accounting.getSaleRegistryList().get(0);
        assertTrue(expectedStoredSaleDTO.getSaleSaleTime().equals(storedSaleDTO.getSaleSaleTime()),
                "SaleTime for sale instance inside accounting is incorrect!");
    }

    @Test
    public void testLogSaleInAccountingSaleBasket() throws ItemDoesNotExistException {
        SaleRegistry accounting = delegator.getSaleRegistry();

        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        contr.logSaleInAccounting();

        SaleDTO expectedStoredSaleDTO = new SaleDTO(contr.getSaleTime(), contr.getBasket(),
                contr.getTotalCost().mathFloor(),
                contr.getTotalVAT(), contr.getPaidAmount(), contr.getChange().mathFloor());

        SaleDTO storedSaleDTO = accounting.getSaleRegistryList().get(0);
        assertTrue(expectedStoredSaleDTO.getSaleBasket().equals(storedSaleDTO.getSaleBasket()),
                "Basket for sale instance inside accounting is incorrect!");
    }

    @Test
    public void testLogSaleInAccountingSaleTotalCost() throws ItemDoesNotExistException {
        SaleRegistry accounting = delegator.getSaleRegistry();

        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        contr.logSaleInAccounting();

        SaleDTO expectedStoredSaleDTO = new SaleDTO(contr.getSaleTime(), contr.getBasket(),
                contr.getTotalCost(),
                contr.getTotalVAT(), contr.getPaidAmount(), contr.getChange());

        SaleDTO storedSaleDTO = accounting.getSaleRegistryList().get(0);

        assertTrue(expectedStoredSaleDTO.getSaleTotalCost().equals(storedSaleDTO.getSaleTotalCost()),
                "TotalCost for sale instance inside accounting is incorrect!");
    }

    @Test
    public void testLogSaleInAccountingSaleTotalVAT() throws ItemDoesNotExistException {
        SaleRegistry accounting = delegator.getSaleRegistry();

        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        contr.logSaleInAccounting();

        SaleDTO expectedStoredSaleDTO = new SaleDTO(contr.getSaleTime(), contr.getBasket(), contr.getTotalCost(),
                contr.getTotalVAT(), contr.getPaidAmount(), contr.getChange());

        SaleDTO storedSaleDTO = accounting.getSaleRegistryList().get(0);

        assertTrue(expectedStoredSaleDTO.getSaleTotalVAT().equals(storedSaleDTO.getSaleTotalVAT()),
                "TotalVAT for sale instance inside accounting is incorrect!");
    }

    @Test
    public void testLogSaleInAccountingSalePaidAmount() throws ItemDoesNotExistException {
        SaleRegistry accounting = delegator.getSaleRegistry();

        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        contr.logSaleInAccounting();

        SaleDTO expectedStoredSaleDTO = new SaleDTO(contr.getSaleTime(), contr.getBasket(), contr.getTotalCost(),
                contr.getTotalVAT(), contr.getPaidAmount(), contr.getChange());

        SaleDTO storedSaleDTO = accounting.getSaleRegistryList().get(0);

        assertTrue(expectedStoredSaleDTO.getSalePaidAmount().equals(storedSaleDTO.getSalePaidAmount()),
                "PaidAmount for sale instance inside accounting is incorrect!");
    }

    @Test
    public void testLogSaleInAccountingSaleChange() throws ItemDoesNotExistException {
        SaleRegistry accounting = delegator.getSaleRegistry();

        contr.scanItem("abc123", 1);
        contr.calcTotal(1);

        contr.pay(new Amount(100));

        contr.logSaleInAccounting();

        SaleDTO expectedStoredSaleDTO = new SaleDTO(contr.getSaleTime(), contr.getBasket(), contr.getTotalCost(),
                contr.getTotalVAT(), contr.getPaidAmount(), contr.getChange());

        SaleDTO storedSaleDTO = accounting.getSaleRegistryList().get(0);

        assertTrue(expectedStoredSaleDTO.getSaleChange().equals(storedSaleDTO.getSaleChange()),
                "Change for sale instance inside accounting is incorrect!");
    }
}