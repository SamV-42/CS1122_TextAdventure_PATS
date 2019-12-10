package world;

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

public class Direction {

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

    private static HashMap<String, Direction> registeredDirectionsById;// = new HashMap<>();

    /*
     * Returns a Direction object if its id matches 
     * @param name The direction id
     * @return null if no such Direction is found, or the chosen direction if found.
     */
    public static Direction getDirectionById(String id) { 
        return registeredDirectionsById.get(id); 
    }

    private String id;
    private String name;

    public Direction(String id, String name) {
        this.id = id;
        this.name = name;

        if(registeredDirectionsById == null) {
            registeredDirectionsById = new HashMap<>();
        }
        Direction oldval = registeredDirectionsById.put(this.getId(), this);
        if(oldval != null) {
            System.err.println("ERROR: Two directions with same id exist; registering the more recent one.\tId: " 
                + this.getId() + "\tOld Value: " + oldval.toString() + "\tNew Value: " + this.toString());
        }
    }

    public Direction(String id) {
        this(id, id);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
