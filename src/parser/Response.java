package parser;

import world.Player;

import java.util.ArrayList;
import java.util.Arrays;

/*
 *  Represents the result of performing a particular command: the message to the player *and* the list of actions that result (call run() to run them)
 *  Note that if any part of this needs customization, you can make an anonynmous inner class to extend it without needing a new file.
 *
 *  Date Last Modified: 12/05/19
 *	@author Sam VanderArk, Patrick Philbin, Thomas Grifka, Alex Hromada
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

public class Response {

    private int severity;
        /*
         * 0 -- This Response should not be run at all
         * >0 -- The higher the severity, the higher priority it will be chosen over others
         * <0 -- reserved for now for special uses, if any end up coming up
         * if an action can't be performed for multiple reasons (eg a player can't
         *  go north, both because the door is locked and the room is dark), then
         *  the more immediate problem is chosen as the response ("you can't see!")
         * - that is, the Response with the higher positive severity.
         */

    private String playerMessage; // The message returned to the player

    private ArrayList<Action> actions = new ArrayList<>();  //The actions, in order, performed during this response
        //Always call getActions() within this class rather than using raw actions, in case it gets overloaded


    public Response(String playerMessage, int severity, Action... actions) {
        this(playerMessage, severity, new ArrayList<Action>(Arrays.asList(actions)));
    }

    public Response(String playerMessage, int severity, ArrayList<Action> actions) {
        setSeverity(severity);
        setPlayerMessage(playerMessage);
        this.actions = actions;
    }

    public Response(String playerMessage, int severity) {
        this(playerMessage, severity, new ArrayList<Action>());
    }

    public Response(String playerMessage) {
        this(playerMessage, 0);
    }

    /*
     * Runs this Response' actions
     * @param player The player who's doing this response
     */
    public void run(Player player) {
        for(Action action : getActions(player) ) {
            action.run(player);
        }
    }

    //Boilerplate below


    /*
     * Gets the severity
     * @return the severity
     */
    public int getSeverity() {
        return severity;
    }

    /*
     * Sets the severity
     * @param severity the severity
     */
    public void setSeverity(int severity) {
        this.severity = severity;
    }

    /*
     * Gets the player message
     * @return the player message
     */
    public String getPlayerMessage(Player player) {
        return playerMessage;
    }

    /*
     * Sets the player message
     * @param playerMessage the player message
     */
    public void setPlayerMessage(String playerMessage) {
        this.playerMessage = playerMessage;
    }

    /*
     * Gets the response's actions
     * @return the response's actions
     */
    public ArrayList<Action> getActions(Player player) {
        return actions; //yes I am aware I should call clone(). No I won't do it.
    }

    /*
     * Sets the response's actions
     * @param actions response's actions
     */
    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }
}
