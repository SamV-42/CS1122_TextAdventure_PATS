package parser.command;

import parser.Action;
import parser.Command;
import parser.Response;
import util.ListMakerHelper;
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

// TODO: "look at <thing>" = "examine <thing>", "examine" = "look"

public class InventoryCommand extends Command {

    public InventoryCommand( boolean register, String id, List<String> names) {
        super(register, id, new Response("", 50, new Action[]{}) {
            @Override
            public String getPlayerMessage(Player player) {
                Item[] inv = player.getInventory();
                if(inv.length == 0) {
                    return "You're not carrying anything at the moment.";
                }
                StringBuilder inventoryMessage = new StringBuilder();
                inventoryMessage.append( "You're carrying " );
                ListMakerHelper help = new ListMakerHelper(inv.length, ".\n");
                for(int i = 0; i < inv.length; ++i ) {
                    inventoryMessage.append(inv[i].getArticle());
                    inventoryMessage.append(" ");
                    inventoryMessage.append(inv[i].getMixin("primaryname").get());
                    inventoryMessage.append(help.getNextSeparator());
                }
                return inventoryMessage.toString();
            }
        }, names);
    }

    public InventoryCommand(String id, List<String> names) {
        this(true, id, names);
    }

    public InventoryCommand(String id, String... names) {
        this(id, new ArrayList<String>(Arrays.asList(names)));
    }

}
