package world;

import util.Composite;
import util.mixin.IdMixin;
import util.mixin.NameMixin;
import util.mixin.PrimaryNameMixin;


import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class Item extends Composite {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Item(String id, List<String> names) {
        addMixin(new IdMixin<>(this, "item", id));
        addMixin(new NameMixin<>(this, "item", names));
        addMixin(new PrimaryNameMixin<>(this, "item", names.get(0) ));
    }

    public Item(String id, String... names) {
        this(id, Arrays.asList(names));
    }

    public String getArticle() {
        List<String> vowels = new ArrayList<>(Arrays.asList(new String[] {"a", "e", "i", "o", "u"}));
        if(vowels.contains(this.<NameMixin>getTypeMixin("name").get()[0].charAt(0))) {
            return "an";
        } else {
            return "a";
        }
    }
}
