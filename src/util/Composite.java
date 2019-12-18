package util;

import java.util.HashMap;

/**
*	This class is extended to represent anything that uses Mixins to effectively have multiple inheritance
*
*   Date Last Modified: 12/18/19
*	@author Thomas Grifka, Sam VanderArk, Patrick Philbin, Alex Hromada
*
*	CS112, Fall 2019
*	Lab Section 2
*/

public class Composite {

    public HashMap<String, Mixin> components = new HashMap<>();     //Each Composite has several mixins

    /*
     * Gets a mixin if present
     * @param id The mixin id
     * @return the mixin, or null if not present
     */
    public Mixin getMixin(String id) {
        return components.get(id);
    }

    /*
     * Gets a mixin if present -- just casts it for type safety
     * @param id The mixin id (yes, you need to include this *and* the type param. Curse type erasure!)
     * @return the mixin, or null if not present
     */
    @SuppressWarnings("unchecked")
    public <T extends Mixin> T getTypeMixin(String id) {
        return (T)(components.get(id));
    }

    /*
     * Adds a mixin
     * @param mix The new mixin
     * @return whether or not it already had that mixin (try to avoid this)
     */
    public boolean addMixin(Mixin mix) {
        return components.put(mix.getMixinId(), mix) != null;
    }

    /*
     * Remvoes a mixin
     * @param id The mixin id to remove
     * @return whether or not it actually had that mixin
     */
    public boolean removeMixin(String id) {
        return components.remove(id) != null;
    }
}
