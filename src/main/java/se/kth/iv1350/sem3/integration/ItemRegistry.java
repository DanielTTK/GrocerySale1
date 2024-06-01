package se.kth.iv1350.sem3.integration;

import java.util.ArrayList;
import java.util.List;

public class ItemRegistry {
    private List<ItemDTO> items = new ArrayList<>();

    /**
     * Creates new instance of ItemRegistry. Also initilizes database.
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

    public void decreaseItemQuanityFromRegistry(String id, int quantity) throws ItemDoesNotExistException { // copied
                                                                                                            // code?
        ItemDTO item = returnItem(id);
        int resultingQuantity = item.getQuantity() - quantity;
        double itemCostToDecrease = item.getCost() - item.getCost() * quantity;

        ItemDTO newItem = new ItemDTO(item.getID(), item.getName(), item.getDescription(),
                resultingQuantity, item.getCost() - itemCostToDecrease, item.getVAT());

        items.set(returnIndexOfItem(id), newItem);
    }

    /**
     * Removes an item from registry.
     * 
     * @param id
     * @throws ItemDoesNotExistException
     */
    public void removeItemFromRegistry(String id) throws ItemDoesNotExistException { // need to adjust it to remove from
                                                                                     // quantity! If q = 0, remove it
                                                                                     // alltogether
        ItemDTO item = returnItem(id);

        if (item.getQuantity() == 1) {
            int indexOfItem = returnIndexOfItem(id);
            items.remove(indexOfItem);
        }

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
     * Checks if item exists.
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
     * Get the list of items as array
     * 
     * @return the list in item inventory
     */

    public ItemDTO[] getInventory() {
        return items.toArray(new ItemDTO[items.size()]);
    }

    /**
     * Get the list of items as arrayList
     * 
     * @return the list in item inventory
     */

    public List<ItemDTO> getInventoryArrayList() {
        return items;
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
