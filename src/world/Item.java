package world;

import util.Composite;
import util.mixin.IdMixin;
import util.mixin.NameMixin;

import java.util.Arrays;
import java.util.List;

public class Item extends Composite {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Item(boolean register, String id, List<String> names) {
        addMixin(new IdMixin<>(this, "item", id));
        addMixin(new NameMixin<>(this, "item", names));
    }

    public Item(String id, List<String> names) {
        this(true, id, names);
    }

    public Item(String id, String... names) {
        this(id, Arrays.asList(names));
    }

    public Item(String id) {
        this(id, new String[]{});
    }

}
