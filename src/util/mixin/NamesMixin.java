package util.mixin;

import util.Composite;
import util.Registration;
import util.StandardMixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*	This class registers names. Especially useful for the altname tag.
*
*   Date Last Modified: 12/18/19
*	@author Thomas Grifka, Sam VanderArk, Patrick Philbin, Alex Hromada
*
*	CS112, Fall 2019
*	Lab Section 2
*/

public class NamesMixin<O extends Composite> extends StandardMixin<O, String> {

    public interface Names {

        /*
         * Get this mixin. Use this if you need to add or remove.
         * @return this mixin
         */
        @SuppressWarnings("unchecked")
        public default NamesMixin getNamesMixin() {
            return ((Composite)this).<NamesMixin>getTypeMixin("name");
        }

        /*
         * Gets a copy of this inventory as an array.
         * @return this inventory
         */
        public default String[] getNames() {
            return getNamesMixin().get();
        }

        /*
         * Gets a copy of this inventory as aa List. Useful for, eg, contains()
         * @return this inventory
         */
        public default List<String> getNamesList() {
            return new ArrayList<>(Arrays.asList(getNames()));
        }
    }

    private Registration<NamesMixin> compRegistrationNames = null;  //The Registration object handling the composite
    private ArrayList<String> names;     //The names for this object

    /*
     * Gets this mixin's id
     * @return this mixin's id
     */
    @Override
    public String getMixinId() { return "name"; }

    public NamesMixin(boolean register, O owner, String className, List<String> names) {
        super(owner, className);

        this.names = new ArrayList<>(names);

        if(register) {
            String id_name = className + "_name";
            compRegistrationNames = new Registration<>(this, id_name);
            for(String name : names) {
                compRegistrationNames.addIdentifier(name);
            }
        }
    }

    public NamesMixin(O owner, String className, List<String> names) {
        this(true, owner, className, names);
    }

    public NamesMixin(O owner, String className, String... names) {
        this(owner, className, Arrays.asList(names));
    }

    /*
     * Gets this mixin's values (an array of names)
     * @return this mixin's values
     */
    @Override
    public String[] get() {
        return names.toArray(new String[]{});
    }

    /*
     * Adds a name to the owner
     * @param t A name to be added
     */
    @Override
    public void add(String t) {
        names.add(t);
        compRegistrationNames.addIdentifier(t);
    }

    /*
     * Removes a name from the owner
     * @param t A name to be removed from the owner
     */
    @Override
    public void remove(String t) {
        names.remove(t);
        compRegistrationNames.removeIdentifier(t);
    }
}
