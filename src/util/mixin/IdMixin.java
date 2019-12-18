package util.mixin;

import util.Composite;
import util.Mixin;
import util.Registration;

/**
*	This class handles an id and registers it.
*
*   Date Last Modified: 12/18/19
*	@author Thomas Grifka, Sam VanderArk, Patrick Philbin, Alex Hromada
*
*	CS112, Fall 2019
*	Lab Section 2
*/

public class IdMixin<O extends Composite> extends Mixin<O, String> {
    public interface Id {
        /*
         * ONLY A COMPOSITE (using the appropriate mixin) SHOULD IMPLEMENT THIS INTERFACE
         * @return this composite's id, IF it has an IdMixin
         */
        @SuppressWarnings("unchecked")
        public default String getId() {
            return ((Composite)this).<IdMixin>getTypeMixin("id").get();
        }
    }

    private String id;  //This composite's id
    private Registration<IdMixin> compRegistrationId = null;    //The Registration object handling the composite

    /*
     * Gets this mixin's id (in a different sense than the id that IdMixin handles!)
     * @return this mixin's id
     */
    @Override
    public String getMixinId() { return "id"; }

    public IdMixin(boolean register, O owner, String className, String id) {
        super(owner, className);
        this.id = id;

        if(register) {
            String id_name = className + "_id";
            compRegistrationId = new Registration<>(this, id_name, id);
        }
    }

    public IdMixin(O owner, String className, String id) {
        this(true, owner, className, id);
    }

    /*
     * Gets this mixin's value (the composite's id)
     * @return the composite's id
     */
    @Override
    public String get() {
        return id;
    }
}
