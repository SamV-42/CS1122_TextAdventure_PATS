package parser;

import world.Player;

/*
 *  A simple wrapper for a bit of runnable code.
 *  I recommend instantiating via lambda expression.
 *
 *  Date Last Modified: 12/05/19
 *	@author Sam VanderArk, Patrick Philbin, Thomas Grifka, Alex Hromada
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

public interface Action {
    /*
     * Define a new action easily with lambdas:
     * eg (play) -> { play.kill(); }
     * @param player the player to whom we apply the action
     */
    public void run(Player player);
}
