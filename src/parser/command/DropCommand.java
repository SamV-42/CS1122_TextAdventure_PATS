package parser.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.lang.StringBuilder;

import parser.*;
import util.*;
import util.mixin.*;
import world.*;


// TODO: "take _ from _" functionality
// TODO: "take" -> "Take what?"
// DONE: "take all"



public class DropCommand extends Command {

    public DropCommand( boolean register, String id, List<String> names) {
        super(register, id, null, names);
    }

    public DropCommand(String id, List<String> names) {
        this(true, id, names);
    }

    public DropCommand(String id, String... names) {
        this(id, new ArrayList<String>(Arrays.asList(names)));
    }

    @Override
    public Response getResponse(String playerInput) {
        DropResponse returnResponse = new DropResponse(100);

        String object = playerInput;
        returnResponse.initialize(object);

        return returnResponse;
    }


    private static class DropResponse extends Response {
        public DropResponse(int severity) {
            super("", severity);
        }

        private String object = null;
        private Item thing = null;

        protected void initialize(String object) {
            this.object = object;
            if(object.equals("all")) {
                thing = null;
            } else {
                thing = Registration.searchOwnerByStr("item_name", object);
            }
        }

        private static enum ItemPresence {
            SUCCESS,
            NOTPRESENT,     //can include present in container nearby
            FAILURE,
            ALL
        }

        private ItemPresence getResult(Player player) {
            if (thing == null) {
                if(object.equals("all")) {
                    return ItemPresence.ALL;
                }
                return ItemPresence.FAILURE;
            } else if (! player.getInventoryList().contains(thing)) {
                return ItemPresence.NOTPRESENT;
            }
            return ItemPresence.SUCCESS;
        }

        @Override
        public String getPlayerMessage(Player player) {
            switch(getResult(player)) {
                case ALL:
                    Item[] inv = player.getInventory();
                    if(inv.length == 0) {
                        return "You don't have anything to drop.";
                    } else {
                        StringBuilder itemsDescription = new StringBuilder();
                        itemsDescription.append("You drop ");

                        ListMakerHelper help = new ListMakerHelper(inv.length, ".\n");
                        for(int i = 0; i < inv.length; ++i ) {
                            itemsDescription.append(inv[i].getArticle());
                            itemsDescription.append(" ");
                            itemsDescription.append(inv[i].getMixin("primaryname").get());
                            itemsDescription.append(help.getNextSeparator());
                        }
                        return itemsDescription.toString();
                    }
                case SUCCESS:
                    return "You drop the " + thing.getPrimaryName() + ".";
                case NOTPRESENT:
                case FAILURE:
                default:
                    return "You don't think you have anything like that.";
            }
        }

        @Override
        public ArrayList<Action> getActions(Player player) {
            ArrayList<Action> actions = new ArrayList<>();
            if(getResult(player) == ItemPresence.SUCCESS) {
                actions.add( (Player p) -> {
                    p.getInventoryMixin().remove(thing);
                    p.getRoom().getInventoryMixin().add(thing);
                });
            } else if(getResult(player) == ItemPresence.ALL) {
                actions.add( (Player p) -> {
                    InventoryMixin<?> playInvMix = p.getInventoryMixin();
                    InventoryMixin<?> roomInvMix = p.getRoom().getInventoryMixin();
                    Item[] inv = playInvMix.get();
                    for(Item item : inv) {
                        playInvMix.remove(item);
                        roomInvMix.add(item);
                    }
                });
            }
            return actions;
        }
    }

}
