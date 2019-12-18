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

    /*
     * What item was being used
     * @return What item was being used, or null if bad noun
     */
    public Item getUsedItem(){
        return usedItem;
    }

    /*
     * The response to this command
     * @param playerInput The player input that led to this command
     * @return The response to this command
     */
    public Response getResponse(String playerInput) {
        String object = playerInput;
        try {
            usedItem = Registration.<Item>searchOwnerByStr("item_name", object).get(0);
        } catch(java.lang.IndexOutOfBoundsException e) { usedItem = null; }

        return new Response("", 50) {
            @Override
            public String getPlayerMessage(Player player) {
                if(usedItem == null || ! player.getInventoryList().contains(usedItem)) {
                    return "You're not holding anything like that.";
                } else {
                    return "You try to use it. Effective?";
                }
            }
        };
    }
}
