package util;

/**
*	This class represents a generic mixin -- a bit of behavior Composites can add (like multiple inheritance!)
*
*   Date Last Modified: 12/18/19
*	@author Thomas Grifka, Sam VanderArk, Patrick Philbin, Alex Hromada
*
*	CS112, Fall 2019
*	Lab Section 2
*/


public abstract class Mixin<O extends Composite,T> {

    private O owner;    //The Composite owning this mixin

    public Mixin(O owner, String className) {
        this.owner = owner;
    }

    /*
     * return this mixin's owner
     * @return this mixin's owner
     */
    public O getOwner() {
        return owner;
    }

    /*
     * Gets whatever data this mixin handles
     * @return whatever data this mixin handles
     */
    public abstract T get();

    /*
     * Gets this mixin's id
     * @return this mixin's id
     */
    public abstract String getMixinId();

}
