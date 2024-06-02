package se.kth.iv1350.sem3.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AmountTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;
    private Amount amount = new Amount(11.1);
    private Amount amount2 = new Amount(5);

    @BeforeEach
    public void renewSetUp() throws IOException {
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);

        amount = new Amount(11.1);
        amount2 = new Amount(5);
    }

    @AfterEach
    public void tearDown() {
        printoutBuffer = null;
        System.setOut(originalSysOut);
    }

    @Test
    public void testMathFloor() {
        Amount number = new Amount(100.111).mathFloor();

        assertTrue(number.equals(new Amount(100.11)), "The number did not get floored");
    }

    @Test
    public void testMathRound() {
        Amount number = (new Amount(100.111)).mathRound();

        assertTrue(number.equals(new Amount(100.1)), "The number did not get rounded");
    }

    @Test
    public void testMultiplyAmt() {
        Amount newAmount = amount.multiplyAmt(2);

        assertTrue(newAmount.equals(new Amount(22.2)));
    }

    @Test
    public void testSubtractAmt() {
        Amount newAmount = amount.subtractAmt(amount2);

        assertTrue(newAmount.equals(new Amount(6.1)));
    }

    @Test
    public void testAddAmt() {
        Amount newAmount = amount.addAmt(amount2);

        assertTrue(newAmount.equals(new Amount(16.1)));
    }

    @Test
    public void testEquals() {
        boolean falseBoolean = amount.equals(amount2);
        boolean trueBoolean = amount.equals(new Amount(11.1));

        assertFalse(falseBoolean, "returned true when supposed to be false");
        assertTrue(trueBoolean, "returned false when supposed to be true");
    }
}
