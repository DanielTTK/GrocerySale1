package se.kth.iv1350.sem3.view;

import org.junit.jupiter.api.Test;

import se.kth.iv1350.sem3.controller.Controller;
import se.kth.iv1350.sem3.controller.GeneralException;
import se.kth.iv1350.sem3.integration.DoesNotExistException;
import se.kth.iv1350.sem3.integration.SystemDelegator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ErrorDisplayTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;

    @BeforeEach
    public void setUp() {
        // Change out old out with new.
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
    }

    @AfterEach
    public void tearDown() {
        printoutBuffer = null;
        System.setOut(originalSysOut);
    }

    @Test
    void testShowErrorToUser() {
        ErrorDisplay error = new ErrorDisplay();
        String testMessage = "This is the test message.";
        String expectedMsg = "ERROR: " + testMessage;
        error.writeErrorMessage(testMessage);

        String result = printoutBuffer.toString();
        assertTrue(result.contains(expectedMsg), "Wrong printout.");
    }

    @Test
    void showInvalidID() throws GeneralException, IOException {
        try {
            SystemDelegator delegator = new SystemDelegator();
            Controller contr = new Controller(delegator);

            System.out.println("Inputting an ID that doesn't exist!");
            contr.startSale();
            contr.scanItem("rnd123", 1);

            throw new DoesNotExistException("rnd123");

        } catch (DoesNotExistException ex) {
            String expectedString = "Cannot recognize identifier " + "rnd123" + ", since it does not exist.";
            assertTrue(ex.getMessage().equals(expectedString), "Wrong message to client!");
        }
    }
}
