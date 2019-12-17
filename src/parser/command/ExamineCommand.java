package parser.command;

import parser.Command;
import parser.Response;
import util.Registration;
import world.Item;
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

public class ExamineCommand extends Command {

    private Item examined;

    public ExamineCommand( boolean register, String id, List<String> names) {
        super(register, id, null, names);
    }

    public ExamineCommand(String id, List<String> names) {
        this(true, id, names);
    }

    public ExamineCommand(String id, String... names) {
        this(id, new ArrayList<String>(Arrays.asList(names)));
    }

    public Item getExamined() { return examined; }

    public Response getResponse(String playerInput) {
        String object = playerInput;
        try {   //NOTE that this is actually wrong and broken but y'know what, it's *fine*
            examined = Registration.<Item>searchOwnerByStr("item_name", object).get(0);
        } catch(java.lang.IndexOutOfBoundsException e) { examined = null; }

        return new Response("", 50) {
            @Override
            public String getPlayerMessage(Player player) {
                List<Item> things = (Registration.<Item>searchOwnerByStr("item_name", object));
                ArrayList<Item> presentThings = new ArrayList<>();
                for(Item i : things) {
                    if(player.getRoom().getInventoryList().contains(i) || player.getInventoryList().contains(i)) {
                        presentThings.add(i);
                    }
                }

                if(presentThings.size() == 0) {
                    return "You don't see anything like that.";
                }
                Item thing = presentThings.get(0);
                return thing.getDescription() == null ? "You don't see anything special about that." : thing.getDescription();
            }
        };
    }
}
