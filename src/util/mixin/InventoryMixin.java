package util.mixin;

import util.Composite;
import util.StandardMixin;
import world.Item;
import util.*;
import util.mixin.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*	This class handles an inventory. Registers under its owner's id, so it should implment IdMixin.
*
*   Date Last Modified: 12/18/19
*	@author Thomas Grifka, Sam VanderArk, Patrick Philbin, Alex Hromada
*
*	CS112, Fall 2019
*	Lab Section 2
*/

public class InventoryMixin<O extends Composite> extends StandardMixin<O, Item> {

    public interface Inventory {

        /*
         * Get this mixin. Use this if you need to add or remove.
         * @return this mixin
         */
        @SuppressWarnings("unchecked")
        public default InventoryMixin getInventoryMixin() {
            return ((Composite)this).<InventoryMixin>getTypeMixin("inventory");
        }

        /*
         * Gets a copy of this inventory as an array.
         * @return this inventory
         */
        public default Item[] getInventory() {
            return getInventoryMixin().get();
        }

        /*
         * Gets a copy of this inventory as aa List. Useful for, eg, contains()
         * @return this inventory
         */
        public default List<Item> getInventoryList() {
            return new ArrayList<>(Arrays.asList(getInventory()));
        }
    }

    private ArrayList<Item> items;      //The items in this inventory
    private Registration<InventoryMixin> compRegistrationInventory = null;  //The Registration object handling the composite

    /*
     * Gets this mixin's id
     * @return this mixin's id
     */
    @Override
    public String getMixinId() { return "inventory"; }

    public InventoryMixin(O owner, String className, List<Item> items) {
        super(owner, className);
        this.items = new ArrayList<>(items);

        String id_name = className + "_inventory";
        compRegistrationInventory = new Registration<>(this, id_name, ((IdMixin.Id)(owner)).getId() );
    }

    public InventoryMixin(O owner, String className, Item... items) {
        this(owner, className, Arrays.asList(items));
    }

    /*
     * Gets this mixin's values (an array of Items)
     * @return this mixin's values
     */
    @Override
    public Item[] get() {
        return items.toArray(new Item[]{});
    }

    /*
     * Adds an item to this inventory
     * @param t An item to be added to the inventory
     */
    @Override
    public void add(Item t) {
        items.add(t);
    }

    /*
     * Removes an item from this inventory
     * @param t An item to be removed from the inventory
     */
    @Override
    public void remove(Item t) {
        items.remove(t);
    }

    /*
     * Returns whether an item is present in this inventory
     * @return whether an item is present in this inventory
     */
    public boolean itemPresent(Item item) {
        return items.contains(item);
    }
}
