package util.mixin;

import parser.Objection;
import util.Composite;
import util.StandardMixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*	This class keeps track of objections associated with an object. Not registered.
*
*   Date Last Modified: 12/18/19
*	@author Thomas Grifka, Sam VanderArk, Patrick Philbin, Alex Hromada
*
*	CS112, Fall 2019
*	Lab Section 2
*/

public class ObjectionMixin<O extends Composite> extends StandardMixin<O, Objection>{

    public interface Objections {

        /*
         * Get this mixin. Use this if you need to add or remove.
         * @return this mixin
         */
        @SuppressWarnings("unchecked")
        public default ObjectionMixin getObjectionMixin() {
            return ((Composite)this).<ObjectionMixin>getTypeMixin("objection");
        }

        /*
         * Gets a copy of this inventory as an array.
         * @return this inventory
         */
        public default Objection[] getObjections() {
            return getObjectionMixin().get();
        }

        /*
         * Gets a copy of this inventory as aa List. Useful for, eg, contains()
         * @return this inventory
         */
        public default List<Objection> getObjectionsList() {
            return new ArrayList<>(Arrays.asList(getObjections()));
        }
    }


    private ArrayList<Objection> objections = new ArrayList<>();    //The objections for this object

    /*
     * Gets this mixin's id
     * @return this mixin's id
     */
    @Override
    public String getMixinId() { return "objection"; }

    public ObjectionMixin(O owner, String className, List<Objection> objections) {
        super(owner, className);
        this.objections = new ArrayList<>(objections);
    }

    public ObjectionMixin(O owner, String className, Objection... objections) {
        this(owner, className, Arrays.asList(objections));
    }

    /*
     * Gets this mixin's values (an array of objections)
     * @return this mixin's values
     */
    @Override
    public Objection[] get() {
        return objections.toArray(new Objection[]{});
    }

    /*
     * Adds an objection to the owner
     * @param t An objection to be added
     */
    @Override
    public void add(Objection t) {
        objections.add(t);
    }

    /*
     * Removes an objection from the owner
     * @param t An objection to be removed from the owner
     */
    @Override
    public void remove(Objection t) {
        objections.remove(t);
    }
}
