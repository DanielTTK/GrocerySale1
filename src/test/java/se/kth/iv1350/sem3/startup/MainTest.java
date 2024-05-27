package se.kth.iv1350.sem3.startup;

import org.junit.jupiter.api.Test;

//import se.kth.iv1350.sem3.controller.Controller;
//import se.kth.iv1350.sem3.startup.Main;
//import se.kth.iv1350.sem3.view.View;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
//import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    // private Main instanceToTest;
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;

    @BeforeEach
    public void setUp() {
        // instanceToTest = new Main();

        // Change out old out with new.
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
    }

    @AfterEach
    public void tearDown() {
        // instanceToTest = null;

        printoutBuffer = null;
        System.setOut(originalSysOut);
    }

    @Test
    public void testUIHasStarted() throws Exception {
        String[] args = null;
        Main.main(args);
        // String printout = printoutBuffer.toString();
        // String expectedOutput = "started";
        // assertTrue(printout.contains(expectedOutput), "UI did not start correctly.");
    }
}
