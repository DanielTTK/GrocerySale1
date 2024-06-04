package se.kth.iv1350.sem3.util;

import se.kth.iv1350.sem3.integration.*;
import se.kth.iv1350.sem3.controller.Controller;
import se.kth.iv1350.sem3.controller.GeneralException;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoggerTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;

    private String logFile = "sem3-log.txt";

    @BeforeEach
    public void setUp() throws IOException {
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
    void testLogException() throws IOException {
        try {
            SystemDelegator delegator = new SystemDelegator();
            Controller contr = new Controller(delegator);

            System.out.println("Trying to induce a database crash");
            contr.startSale();
            contr.scanItem("err111", 1);
        } catch (GeneralException ex) {
            String expectedString = "Critical database failure";
            assertTrue(fileContains(expectedString), "Wrong logger message!");
        } catch (DoesNotExistException ex) {

        }
    }

    private boolean fileContains(String searched) throws IOException {
        BufferedReader txt = new BufferedReader(new FileReader(logFile));
        String line = null;
        while ((line = txt.readLine()) != null) {
            if (line.contains(searched)) {
                txt.close();
                return true;
            }
        }
        txt.close();
        return false;
    }
}
