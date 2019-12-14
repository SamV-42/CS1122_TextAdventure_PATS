package parser.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import world.Player;
import parser.Command;
import parser.Response;
import util.Registration;
import world.Item;

/*
 *  Represents a command that shows the room
 *
 *  Date Last Modified: 12/05/19
 *	@author Sam VanderArk, Patrick Philbin, Thomas Grifka, Alex Hromada
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

public class ExamineCommand extends Command {

    public ExamineCommand( boolean register, String id, List<String> names) {
        super(register, id, null, names);
    }

    public ExamineCommand(String id, List<String> names) {
        this(true, id, names);
    }

    public ExamineCommand(String id, String... names) {
        this(id, new ArrayList<String>(Arrays.asList(names)));
    }

    public Response getResponse(String playerInput) {
        String object = playerInput;
        return new Response("", 50) {
            @Override
            public String getPlayerMessage(Player player) {
                Item thing = Registration.searchOwnerByStr("item_name", object);
                if(thing == null) {
                    return "You don't see anything like that.";
                } else {
                    return thing.getDescription() == null ? "You don't see anything special about that." : thing.getDescription();
                }
            }
        };
    }
}
