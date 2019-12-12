package util;

import java.util.HashMap;

public class Composite {

    public HashMap<String, Mixin> components = new HashMap<>();

    public Mixin getMixin(String id) {
        return components.get(id);
    }

    @SuppressWarnings("unchecked")
    public <T extends Mixin> T getTypeMixin(String id) {
        return (T)(components.get(id));
    }

    public boolean addMixin(Mixin mix) {
        return components.put(mix.getMixinId(), mix) != null;
    }

    public boolean removeMixin(String id) {
        return components.remove(id) != null;
    }
}
