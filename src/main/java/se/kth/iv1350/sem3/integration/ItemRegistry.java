package se.kth.iv1350.sem3.integration;

import java.util.ArrayList;
import java.util.List;

import se.kth.iv1350.sem3.model.Amount;

/**
 * Contains all calls that has to do with inventory.
 */
public class ItemRegistry {
    private List<ItemDTO> items = new ArrayList<>();

    /**
     * Creates new instance of <code>ItemRegistry</code>. Also initilizes database.
     */
    public ItemRegistry() {
        addAllItems();
    }

    /**
     * gets items from <code>ItemRegistry</code> and returns specific one with
     * specific identifier.
     * 
     * @param id id of product you want to get
     * @throws ItemRegistryException gets called when something is wrong
     *                               with database. Checked.
     * @throws DoesNotExistException throws when identifier is not in database.
     *                               Unchecked.
     */
    public ItemDTO returnItem(String id) throws DoesNotExistException {
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
     * Checks if item exists.
     * 
     * @param item   Item object to check
     * @param itemID Searched id
     * @throws DoesNotExistException
     */
    private void checkIfItemExist(ItemDTO item, String itemID) throws DoesNotExistException {
        if (item == null) {
            throw new DoesNotExistException(itemID);
        }
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
     * Decreases quantity from an item in registry.
     * 
     * @param id
     * @param quantity
     * @throws DoesNotExistException
     */
    public void decreaseItemQuanityFromRegistry(String id, int quantity) throws DoesNotExistException { // copied
                                                                                                        // code?
        ItemDTO item = returnItem(id);
        int resultingQuantity = item.getQuantity() - quantity;
        Amount itemCostToDecrease = item.getCost().subtractAmt(item.getCost().multiplyAmt(quantity));

        if (resultingQuantity <= 0) {// add exception when quantity is negative on sem4
            removeItemFromRegistry(id);
            return;
        }

        ItemDTO newItem = new ItemDTO(item.getID(), item.getName(), item.getDescription(),
                resultingQuantity, item.getCost().subtractAmt(itemCostToDecrease), item.getVAT());

        items.set(returnIndexOfItem(id), newItem);
    }

    /**
     * Removes an item from registry. Generally used to ensure that
     * something gets removed if it already doesn't when quantity is 0.
     * 
     * @param id
     * @throws DoesNotExistException
     */
    public void removeItemFromRegistry(String id) throws DoesNotExistException { // make private?
        int indexOfItem = returnIndexOfItem(id);
        items.remove(indexOfItem);
    }

    /**
     * Finds <code>index</code> in database by searching for identifier. Finds
     * latest index of identical items.
     * 
     * @param id identifier of item
     * @return returns index of item
     * @throws DoesNotExistException
     */
    public int returnIndexOfItem(String id) throws DoesNotExistException {
        ItemDTO[] itemArray = getInventory();
        int index = -1;

        for (int i = 0; i < itemArray.length; i++) {
            if (id == itemArray[i].getID()) {
                index = i;
            }
        }

        if (index == -1) {
            throw new DoesNotExistException(id);
        }

        return index;
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
     * Hard coded database, this should be connected to some external
     * UI.
     * Note: Every object in inventory are seperate elements. This list does not
     * have the same functionality as basket in Sale, where quantity adds up for
     * identical items.
     */
    private void addAllItems() {
        items.add(new ItemDTO("abc123", "BigWheel Oatmeal",
                "BigWheel Oatmeal 500 g, whole grain oats, high fiber, gluten free", 2, new Amount(29.90), 0.06));

        items.add(new ItemDTO("def456", "YouGoGo Blueberry",
                "YouGoGo Blueberry 240 g, low sugar yoghurt, blueberry flavour", 1, new Amount(14.90), 0.06));
    }
}
