package parser.command;

import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import world.Direction;
import world.Player;
import world.Room;
import parser.Command;
import parser.Response;
import parser.Action;

/*
 *  Represents a command that shows the room
 *
 *  Date Last Modified: 12/05/19
 *	@author Sam VanderArk, Patrick Philbin, Thomas Grifka, Alex Hromada
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

public class LookCommand extends Command {

    public LookCommand(String id, List<String> names, boolean register) {
        super(id, new Response("", 50, new Action[]{}) {
            @Override
            public String getPlayerMessage(Player player) {
                return player.getRoom().look();
            }
        }, names, register);
    }

    public LookCommand(String id, List<String> names) {
        this(id, names, true);
    }

    public LookCommand(String id, String... names) {
        this(id, new ArrayList<String>(Arrays.asList(names)));
    }

}
