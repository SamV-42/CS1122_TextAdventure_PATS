package util.mixin;

import util.Composite;
import util.StandardMixin;
import util.Registration;
import world.Item;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

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

    @Override
    public String getMixinId() { return "inventory"; }

    public InventoryMixin(O owner, String className, List<Item> items) {
        super(owner, className);
        this.items = new ArrayList<>(items);
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
