package se.kth.iv1350.sem3.integration;

import java.util.ArrayList;
import java.util.List;

public class ItemRegistry {
    private List<ItemDTO> items = new ArrayList<>();

    /**
     * Creates new instance of ItemRegistry. Also initilizes database.
     * 
     * @param contr The controller that is used for all operations.
     */
    public ItemRegistry() {
        addAllItems();
    }

    /**
     * gets items from ItemRegistry and returns specific one with specific
     * identifier.
     * 
     * @param id id of product you want to get
     * @throws ItemRegistryException     gets called when something is wrong
     *                                   with database. Checked.
     * @throws ItemDoesNotExistException throws when identifier is not in database.
     *                                   Unchecked.
     */
    public ItemDTO returnItem(String id) throws ItemDoesNotExistException {
        ItemDTO[] itemArray = getInventory();
        ItemDTO item = null;

        inducedDatabaseError(id);

        for (int i = 0; i < itemArray.length; i++) {
            if (id == itemArray[i].getID()) {
                item = itemArray[i];
            }
        }
        checkIfItemExist(item, id);
        return item;
    }

    /**
     * Creates an database error deliberately by hardcoding a special id to throw
     * this.
     * 
     * @param id if this equals "err111" then error is thrown.
     */
    private void inducedDatabaseError(String id) {
        if (id.equals("err111")) {
            throw new ItemRegistryException("Critical database failure");
        }
    }

    /**
     * Removes an item from registry.
     * 
     * @param id
     * @throws ItemDoesNotExistException
     */
    public void removeItemFromRegistry(String id) throws ItemDoesNotExistException {
        int indexOfItem = returnIndexOfItem(id);
        items.remove(indexOfItem);
    }

    /**
     * Finds index in database by searching for identifier.
     * 
     * @param id identifier of item
     * @return returns index of item
     * @throws ItemDoesNotExistException
     */
    public int returnIndexOfItem(String id) throws ItemDoesNotExistException {
        ItemDTO[] itemArray = getInventory();
        int index = -1;

        for (int i = 0; i < itemArray.length; i++) {
            if (id == itemArray[i].getID()) {
                index = i;
            }
        }

        if (index == -1) {
            throw new ItemDoesNotExistException(id);
        }

        return index;
    }

    /**
     * Checks if item is null or not. If null throws exception detailed in throws.
     * 
     * @param item   Item object to check
     * @param itemID Searched id
     * @throws DoesNotExistException
     */
    private void checkIfItemExist(ItemDTO item, String itemID) throws ItemDoesNotExistException {
        if (item == null) {
            throw new ItemDoesNotExistException(itemID);
        }
    }

    /**
     * Get the list of items
     * 
     * @return the list in item inventory
     */

    public ItemDTO[] getInventory() {
        return items.toArray(new ItemDTO[items.size()]);
    }

    /**
     * Hard coded database, this should be connected to some
     * UI. Using normal array, arraylist to be implemented later for .add()
     * function.
     */
    private void addAllItems() {
        items.add(new ItemDTO("abc123", "BigWheel Oatmeal",
                "BigWheel Oatmeal 500 g, whole grain oats, high fiber, gluten free", 1, 29.90, 0.06));

        items.add(new ItemDTO("abc123", "BigWheel Oatmeal",
                "BigWheel Oatmeal 500 g, whole grain oats, high fiber, gluten free", 1, 29.90, 0.06));

        items.add(new ItemDTO("def456", "YouGoGo Blueberry",
                "YouGoGo Blueberry 240 g, low sugar yoghurt, blueberry flavour", 1, 14.90, 0.06));
    }
}
