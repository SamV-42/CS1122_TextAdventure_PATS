import util.NamedObject;

import java.util.Arrays;
import java.util.List;

public class Item extends NamedObject {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /* Constructors specified in NamedObject */
    public Item(String id, List<String> names, boolean register) {
        super("item", id, names, register);
    }
    public Item(String id, List<String> names) { this(id, names, true); }
    public Item(String id, String... names) { this(id, Arrays.asList(names)); }
    public Item(String id) {this(id, new String[]{}); }

}
