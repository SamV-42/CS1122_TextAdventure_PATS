package parser;

import world.Player;

/*
 *  Represents anything that would prevent a player-character from performing a particular action.
 *  I recommend instantiating via lambda expression.
 * 
 *  Example: LockedDoorObjection (prevents the action if the player tries to move through a particular exit without a key)
 *  Example: MinotaurObjection (kills the player if they do anything to wake up the minotaur)
 *
 *  Date Last Modified: 12/05/19
 *	@author Sam VanderArk, Patrick Philbin, Thomas Grifka, Alex Hromada
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

public interface Objection {
    /*
     * Checks if it's applicable to the action, and returns a runnable response if so.
     * @param The proposed action
     * @return null if this objection doesn't apply; a Response if it does.
     */
    public Response check(Player player, Command command);
}
