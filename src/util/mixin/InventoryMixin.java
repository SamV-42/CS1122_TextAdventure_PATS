package util.mixin;

import util.Composite;
import util.StandardMixin;
import world.Item;
import util.*;
import util.mixin.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryMixin<O extends Composite> extends StandardMixin<O, Item> {

    public interface Inventory {

        @SuppressWarnings("unchecked")
        public default InventoryMixin getInventoryMixin() {
            return ((Composite)this).<InventoryMixin>getTypeMixin("inventory");
        }

        public default Item[] getInventory() {
            return getInventoryMixin().get();
        }

        public default List<Item> getInventoryList() {
            return new ArrayList<>(Arrays.asList(getInventory()));
        }
    }

    private ArrayList<Item> items;
    private Registration<InventoryMixin> compRegistrationInventory = null;

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

    @Override
    public Item[] get() {
        return items.toArray(new Item[]{});
    }

    @Override
    public void add(Item t) {
        items.add(t);
    }

    @Override
    public void remove(Item t) {
        items.remove(t);
    }

    public boolean itemPresent(Item item) {
        return items.contains(item);
    }
}
