package util.mixin;

import util.Composite;
import util.Mixin;


/**
*	This class handles a primary (probably player-visible) name and registers it.
*
*   Date Last Modified: 12/18/19
*	@author Thomas Grifka, Sam VanderArk, Patrick Philbin, Alex Hromada
*
*	CS112, Fall 2019
*	Lab Section 2
*/

public class PrimaryNameMixin<O extends Composite> extends Mixin<O, String> {

    public interface PrimaryName {

        /*
         * Get this mixin. Use this if you need to add or remove.
         * @return this mixin
         */
        @SuppressWarnings("unchecked")
        public default PrimaryNameMixin getPrimaryNameMixin() {
            return ((Composite)this).<PrimaryNameMixin>getTypeMixin("primaryname");
        }

        /*
         * Gets this's primary name
         * @return this's primary name
         */
        public default String getPrimaryName() {
            return getPrimaryNameMixin().get();
        }

        /*
         * Sets this's primary name
         * @param name this's primary name
         */
        public default void setPrimaryName(String name) {
            getPrimaryNameMixin().set(name);
        }
    }

    private String name;    //The primary name in question

    /*
     * Gets this mixin's id
     * @return this mixin's id
     */
    @Override
    public String getMixinId() { return "primaryname"; }

    public PrimaryNameMixin(O owner, String className, String name) {
        super(owner, className);
        this.name = name;
    }

    /*
     * Gets this mixin's value (the composite's primary name)
     * @return the composite's primary name
     */
    @Override
    public String get() {
        return name;
    }

    /*
     * Sets this mixin's value (the composite's primary name)
     * @param name the composite's primary name
     */
    public void set(String name) {
        this.name = name;
    }
}
