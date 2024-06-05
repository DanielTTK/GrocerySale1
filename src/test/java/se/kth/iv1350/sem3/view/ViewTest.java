package se.kth.iv1350.sem3.view;

import org.junit.jupiter.api.Test;

import se.kth.iv1350.sem3.controller.Controller;
import se.kth.iv1350.sem3.integration.ItemDTO;
import se.kth.iv1350.sem3.integration.SystemDelegator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ViewTest {
    private View instanceToTest;
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;
    private Controller contr;

    @BeforeEach
    public void setUp() throws IOException {
        SystemDelegator delegator = new SystemDelegator();
        contr = new Controller(delegator);
        instanceToTest = new View(contr);

        // Change out old out with new.
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
        instanceToTest.runFakeExecution();
    }

    @AfterEach
    public void tearDown() {
        instanceToTest = null;
        contr = null;
        printoutBuffer = null;
        System.setOut(originalSysOut);
    }

    @Test
    void testRunFakeExecution() {
        String printout = printoutBuffer.toString();
        System.out.println(printout);
        String expectedOutput = "Sale has been started.";
        assertTrue(printout.contains(expectedOutput), "UI did not start correctly.");
    }

    @Test
    public void testViewChange() {
        String expectedChangeInStringForm = "Change to give the customer: "
                + contr.getPaidAmount().subtractAmt(contr.getTotalCost()).mathFloor()
                        .toString();

        assertTrue(printoutBuffer.toString().contains(expectedChangeInStringForm), "Invalid change in view output!");
    }

    @Test
    public void testViewItemCost() {
        ItemDTO item = contr.getBasket().get(0);
        String expectedItemCostString = item.getCost().toString();

        assertTrue(printoutBuffer.toString().contains(expectedItemCostString), "Invalid item cost in view output!");
    }

    @Test
    public void testViewTotalCost() {
        String expectedTotalCostString = "Total: " + contr.getTotalCost().mathFloor().toString() + " SEK";

        assertTrue(printoutBuffer.toString().contains(expectedTotalCostString), "Invalid total cost in view output!");
    }

    @Test
    public void testViewTotalVAT() {
        String expectedTotalVATString = "VAT: " + contr.getTotalVAT().mathFloor().toString();

        assertTrue(printoutBuffer.toString().contains(expectedTotalVATString), "Invalid total vat in view output!");
    }

    @Test
    public void testViewPaidAmount() {
        String expectedPaidAmountString = "Payment: " + contr.getPaidAmount().toString();

        assertTrue(printoutBuffer.toString().contains(expectedPaidAmountString), "Invalid paid amount in view output!");
    }

    @Test
    public void testViewQuantity() {
        String expectedQuantityString = "Add 1";

        assertTrue(printoutBuffer.toString().contains(expectedQuantityString), "Invalid quantity in view output!");
    }
}
