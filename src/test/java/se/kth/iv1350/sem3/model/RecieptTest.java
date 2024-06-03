package se.kth.iv1350.sem3.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.sem3.integration.ItemDTO;
import se.kth.iv1350.sem3.integration.ItemDoesNotExistException;
import se.kth.iv1350.sem3.integration.SystemDelegator;

public class RecieptTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;
    private Sale sale;
    private Reciept reciept;
    private SystemDelegator delegator;

    private String recieptString;

    @BeforeEach
    public void renewSetUp() throws IOException, ItemDoesNotExistException {
        delegator = new SystemDelegator();
        sale = new Sale(delegator);
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
        reciept = new Reciept(sale);

        sale.addItemToBasket("abc123", 1);
        sale.calcTotal(1);
        sale.finishSale(new Amount(100));

        recieptString = reciept.createRecieptDigital();
    }

    @AfterEach
    public void tearDown() {
        delegator = null;
        sale = null;
        printoutBuffer = null;
        System.setOut(originalSysOut);
    }

    @Test
    public void testCreateRecieptChange() throws ItemDoesNotExistException {
        String expectedChangeInStringForm = sale.getPaidAmount().subtractAmt(sale.getTotalCost()).mathFloor()
                .toString();

        assertTrue(recieptString.contains(expectedChangeInStringForm), "Invalid change in reciept!");
    }

    @Test
    public void testCreateRecieptItemCost() throws ItemDoesNotExistException {
        ItemDTO item = sale.getBasket().get(0);
        String expectedItemCostString = item.getCost().toString();

        assertTrue(recieptString.contains(expectedItemCostString), "Invalid item cost in reciept!");
    }

    @Test
    public void testCreateRecieptTotalCost() throws ItemDoesNotExistException {
        String expectedTotalCostString = sale.getTotalCost().mathFloor().toString();

        assertTrue(recieptString.contains(expectedTotalCostString), "Invalid total cost in reciept!");
    }

    @Test
    public void testCreateRecieptTotalVAT() throws ItemDoesNotExistException {
        String expectedTotalVATString = sale.getTotalVAT().mathFloor().toString();

        assertTrue(recieptString.contains(expectedTotalVATString), "Invalid total vat in reciept!");
    }

    @Test
    public void testCreateRecieptPaidAmount() throws ItemDoesNotExistException {
        String expectedPaidAmountString = sale.getPaidAmount().toString();

        assertTrue(recieptString.contains(expectedPaidAmountString), "Invalid paid amount in reciept!");
    }

    @Test
    public void testCreateRecieptQuantity() throws ItemDoesNotExistException {
        String expectedQuantityString = "1";

        assertTrue(recieptString.contains(expectedQuantityString), "Invalid quantity in reciept!");
    }
}
