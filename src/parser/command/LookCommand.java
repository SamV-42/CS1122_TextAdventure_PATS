package parser.command;

import parser.Action;
import parser.Command;
import parser.Response;
import world.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 *  Represents a command that shows the room
 *
 *  Date Last Modified: 12/05/19
 *	@author Sam VanderArk, Patrick Philbin, Thomas Grifka, Alex Hromada
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

// TODO: "look at <thing>" = "examine <thing>", "examine" = "look"

public class LookCommand extends Command {

    public LookCommand( boolean register, String id, List<String> names) {
        super(register, id, new Response("", 50, new Action[]{}) {
            @Override
            public String getPlayerMessage(Player player) {
                return player.getRoom().look();
            }
        }, names);
    }

    public LookCommand(String id, List<String> names) {
        this(true, id, names);
    }

    public LookCommand(String id, String... names) {
        this(id, new ArrayList<String>(Arrays.asList(names)));
    }

}
