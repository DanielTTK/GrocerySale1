package se.kth.iv1350.sem3.integration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemRegistryTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;
    private SystemDelegator delegator;
    private ItemRegistry itemRegistry;

    @BeforeEach
    public void renewSetUp() throws IOException {
        delegator = new SystemDelegator();
        itemRegistry = delegator.getItemRegistry();
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
    public void testReturnItem() throws ItemDoesNotExistException {
        ItemDTO item = itemRegistry.returnItem("abc123");

        assertTrue(item.getID() == "abc123", "Wrong item returned!");
    }

    @Test
    public void testDecreaseItemQuanityFromRegistry() throws ItemDoesNotExistException {
        itemRegistry.decreaseItemQuanityFromRegistry("abc123", 1);
        itemRegistry.decreaseItemQuanityFromRegistry("def456", 1);

        ItemDTO item1 = itemRegistry.getInventoryArrayList().get(0);

        assertTrue(item1.getQuantity() == 1, "Quantity did not decrease");
        assertTrue(itemRegistry.getInventoryArrayList().size() == 1, "Length is wrong, it is: " + itemRegistry
                .getInventoryArrayList().size());
    }

    @Test
    public void testRemoveItemFromRegistry() throws ItemDoesNotExistException {
        itemRegistry.removeItemFromRegistry("def456");

        assertTrue(itemRegistry.getInventoryArrayList().size() == 2, "Length is wrong, it is: " + itemRegistry
                .getInventoryArrayList().size());
    }

    @Test
    public void testReturnIndexOfItem() throws ItemDoesNotExistException {
        int indexItem = itemRegistry.returnIndexOfItem("abc123");

        assertTrue(indexItem == 1,
                "wrong index " + indexItem + " " + itemRegistry.getInventoryArrayList().get(0).getID());
        // This method, returnIndexOfItem(), takes the latest of two same items. So for
        // example, in inventory there exist "abc123" on index 0 but also on index 1.
        // This is okay and intended as the method doesn't care and doesn't get used in
        // a way where a different order is required.
    }
}
