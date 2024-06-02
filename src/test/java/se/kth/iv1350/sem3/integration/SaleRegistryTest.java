package se.kth.iv1350.sem3.integration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.sem3.model.Amount;

import static org.junit.jupiter.api.Assertions.*;

public class SaleRegistryTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;
    private SystemDelegator delegator;
    private SaleRegistry saleRegistry;

    @BeforeEach
    public void renewSetUp() throws IOException {
        delegator = new SystemDelegator();
        saleRegistry = delegator.getSaleRegistry();
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
    }

    @AfterEach
    public void tearDown() {
        delegator = null;
        printoutBuffer = null;
        System.setOut(originalSysOut);
    }

    @Test
    public void testSaveItem() {
        SaleDTO dto = new SaleDTO(null, null, new Amount(0), new Amount(0), new Amount(0), new Amount(0));
        saleRegistry.saveSale(dto);

        int outcome = saleRegistry.getSaleRegistry().size();
        int expectedOutcome = 1;

        assertTrue(expectedOutcome == outcome, "Nothing was added to the saleRegistry");
    }
}
