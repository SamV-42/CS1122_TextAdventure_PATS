package world;

import parser.Parser;
import util.Composite;
import util.Registration;
import util.mixin.IdMixin;
import util.mixin.InventoryMixin;
import util.mixin.ObjectionMixin;
import util.mixin.PrimaryNameMixin;

/**
 *	Player Object
 *
 *       Date Last Modified: 12/18/2019
 *	@author Thomas Grifka, Sam VanderArk, Patrick Philbin, Alex Hromada
 *
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

public class Player extends Composite
        implements IdMixin.Id, ObjectionMixin.Objections, PrimaryNameMixin.PrimaryName, InventoryMixin.Inventory {

    private Room room; //the current room of the player
    private boolean host; //if the player is host or not
    private long connectionId; //the connectionID of the player
    private Parser parser = new Parser(); //parser to handle the kill command
    private boolean alive = true; //whether or not the player is alive
    private boolean inLab = false; //whether or not the player is in the labryinth
    private boolean hasWon = false; //whether or not the minotaur has been killed yet

    /**
     * Constructor for the player object
     * @param id id of the player, aka username
     * @param connectionId connection id of the client controlling the player
     */
    public Player(String id, long connectionId) {
        addMixin(new IdMixin<>(this, "player", id));
        addMixin(new ObjectionMixin<>(this, "player"));
        addMixin(new PrimaryNameMixin<>(this, "player", id.toUpperCase()));
        addMixin(new InventoryMixin<>(this, "player"));
        this.connectionId = connectionId;
    }

    /**
     * gets the current room of the Player
     * @return room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * sets the room of the player
     * @param room new room of player
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * sets whether this player is the "host"
     * @param host true if is the "host"
     */
    public void setHost(boolean host){
        this.host = host;
    }

    /**
     * returns true if the player is the "host"
     * @return host
     */
    public boolean isHost() {
        return host;
    }

    /**
     * gets the connectionId of the player
     * @return connectionID
     */
    public long getConnectionID () {
        return connectionId;
    }

    /**
     * sets the connectionID of the player
     * @param id the new connectiion id
     */
    public void setConnectionID(long id){ connectionId = id; }

    /**
     * resets the player, acts as a kill function
     */
    public void kill(){
        parser.runPlayerInput(this, "drop all");
        alive = false;
        room = new Room("hell");
    }

    /**
     * returns wheter or not the player is alive
     * @return !alive
     */
    public boolean isDead(){
        return !alive;
    }

    /**
     * sets the boolean inLab to true idicating the player is in the labryinth
     */
    public void setInLab(){
        inLab = true;
    }

    /**
     * returns true if the player is in the labryinth
     * @return
     */
    public boolean inLab(){
        return inLab;
    }

    /**
     * sets the haswon boolean
     * @param hasWon only used if true
     */
    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    /**
     * returns if the players have won or not
     * @return hasWon
     */
    public boolean hasWon() {
        return hasWon;
    }
}