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

public class UseCommand extends Command {

    private Item usedItem;

    public UseCommand( boolean register, String id, List<String> names) {
        super(register, id, null, names);
    }

    public UseCommand(String id, List<String> names) {
        this(true, id, names);
    }

    public UseCommand(String id, String... names) {
        this(id, new ArrayList<String>(Arrays.asList(names)));
    }

    public Item getUsedItem(){
        return usedItem;
    }

    public Response getResponse(String playerInput) {
        String object = playerInput;
        Item thing = Registration.<Item>searchOwnerByStr("item_name", object).get(0);
        usedItem = thing;
        return new Response("", 50) {
            @Override
            public String getPlayerMessage(Player player) {
                Item thing = (Registration.<Item>searchOwnerByStr("item_name", object)).get(0);
                if(thing == null || ! player.getInventoryList().contains(thing)) {
                    return "You're not holding anything like that.";
                } else {
                    return "You try to use it. Effective?";
                }
            }
        };
    }
}
