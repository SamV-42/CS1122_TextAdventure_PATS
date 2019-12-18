package util;

/**
*	This class represents a mixin handling a list of objects for an owner.
*
*   Date Last Modified: 12/18/19
*	@author Thomas Grifka, Sam VanderArk, Patrick Philbin, Alex Hromada
*
*	CS112, Fall 2019
*	Lab Section 2
*/

public abstract class StandardMixin<O  extends Composite, T> extends Mixin<O, T[]> {

    public StandardMixin(O owner, String className) {
        super(owner, className);
    }

    /*
     * Adds an item to this mixin
     * @param t An item to be added to the list
     */
    public abstract void add(T t);

    /*
     * Removes an item from this mixin
     * @param t An item to be removed from the list
     */
    public abstract void remove(T t);
}
