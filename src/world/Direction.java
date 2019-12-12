package world;

import util.Registration;
import util.Composite;
import util.mixin.IdMixin;
import util.mixin.NameMixin;
import util.mixin.PrimaryNameMixin;

import java.util.ArrayList;
import java.util.HashMap;

/*
 *  Represents a direction relating rooms in the world.
 *  Note that this is distinct from the actual typed command needed to move
 *   -- that class is DirectionCommand, and the specific accepted words are defined in DataLoader
 *
 *  Date Last Modified: 12/05/19
 *	@author Sam VanderArk, Patrick Philbin, Thomas Grifka, Alex Hromada
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

public class Direction extends Composite {

    // All of the below are put here for convenience' sake (over needing to type, eg, getDirectionByName("north") each time)
    // Their names are self-explanatory.
    public static final Direction NORTH = new Direction("north");
    public static final Direction SOUTH = new Direction("south");
    public static final Direction EAST = new Direction("east");
    public static final Direction WEST = new Direction("west");
    public static final Direction NORTHEAST = new Direction("northeast");
    public static final Direction SOUTHEAST = new Direction("northwest");
    public static final Direction NORTHWEST = new Direction("southeast");
    public static final Direction SOUTHWEST = new Direction("southwest");
    public static final Direction UP = new Direction("up");
    public static final Direction DOWN = new Direction("down");
    public static final Direction IN = new Direction("in");
    public static final Direction OUT = new Direction("out");

    private Registration<Direction> compRegistration = null;

    private String id;

    public Direction(String id, String name) {
        addMixin(new IdMixin<>(this, "direction", id));
        addMixin(new PrimaryNameMixin<>(this, "direction", name));
    }

    public Direction(String id) {
        this(id, id);
    }
}
