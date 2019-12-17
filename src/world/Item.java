package world;

import util.Composite;
import util.mixin.IdMixin;
import util.mixin.NamesMixin;
import util.mixin.PrimaryNameMixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Item extends Composite
        implements IdMixin.Id, NamesMixin.Names, PrimaryNameMixin.PrimaryName {

    private String description;
    private boolean staticPickup = false;
    private boolean hidden = false;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Item(String id, List<String> names) {
        addMixin(new IdMixin<>(this, "item", id));
        addMixin(new NamesMixin<>(this, "item", names));
        addMixin(new PrimaryNameMixin<>(this, "item", names.get(0) ));
    }

    public Item(String id, String... names) {
        this(id, Arrays.asList(names));
    }

    public String getArticle() {
        List<Character> vowels = new ArrayList<>(Arrays.asList(new Character[] {'a', 'e', 'i', 'o', 'u'}));
        if(vowels.contains(this.getPrimaryName().charAt(0))) {
            return "an";
        } else {
            return "a";
        }
    }

    public boolean getStatic() {
        return staticPickup;
    }

    public void setStatic(boolean staticPickup) {
        this.staticPickup = staticPickup;
    }

    public boolean getHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
