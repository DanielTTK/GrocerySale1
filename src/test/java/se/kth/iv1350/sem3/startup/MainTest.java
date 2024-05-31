package se.kth.iv1350.sem3.startup;

import org.junit.jupiter.api.Test;

//import se.kth.iv1350.sem3.controller.Controller;
//import se.kth.iv1350.sem3.startup.Main;
//import se.kth.iv1350.sem3.view.View;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;

    @BeforeEach
    public void renewSetUp() {

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
    public void testUIHasStarted() throws Exception {
        String[] args = null;
        Main.main(args);
        String printout = printoutBuffer.toString();
        String expectedOutput = "Sale has been started.";
        assertTrue(printout.contains(expectedOutput), "UI did not start correctly.");
    }
}
