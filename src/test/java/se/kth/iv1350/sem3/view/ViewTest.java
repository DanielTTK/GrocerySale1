package se.kth.iv1350.sem3.view;

import org.junit.jupiter.api.Test;

import se.kth.iv1350.sem3.controller.Controller;
import se.kth.iv1350.sem3.integration.ItemDoesNotExistException;
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

    @BeforeEach
    public void setUp() throws IOException {
        SystemDelegator delegator = new SystemDelegator();
        Controller contr = new Controller(delegator);
        instanceToTest = new View(contr);

        // Change out old out with new.
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
    }

    @AfterEach
    public void tearDown() {
        instanceToTest = null;

        printoutBuffer = null;
        System.setOut(originalSysOut);
    }

    @Test
    void testRunFakeExecution() throws ItemDoesNotExistException {
        instanceToTest.runFakeExecution();
        String printout = printoutBuffer.toString();
        System.out.println(printout);
        String expectedOutput = "Sale has been started.";
        assertTrue(printout.contains(expectedOutput), "UI did not start correctly.");
    }
}
