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



public class TakeCommand extends Command {

    public TakeCommand( boolean register, String id, List<String> names) {
        super(register, id, null, names);
    }

    public TakeCommand(String id, List<String> names) {
        this(true, id, names);
    }

    public TakeCommand(String id, String... names) {
        this(id, new ArrayList<String>(Arrays.asList(names)));
    }

    @Override
    public Response getResponse(String playerInput) {
        TakeResponse returnResponse = new TakeResponse(100);

        String object = playerInput;
        returnResponse.initialize(object);

        return returnResponse;
    }


    private static class TakeResponse extends Response {
        public TakeResponse(int severity) {
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
            STATIC,
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
            } else if (! player.getRoom().getInventoryList().contains(thing)) {
                return ItemPresence.NOTPRESENT;
            } else if(thing.getStatic()) {
                return ItemPresence.STATIC;
            }
            return ItemPresence.SUCCESS;
        }

        @Override
        public String getPlayerMessage(Player player) {
            switch(getResult(player)) {
                case ALL:
                    Item[] inv = player.getRoom().getInventory();
                    if(inv.length == 0) {
                        return "You don't see much else to take.";
                    } else {
                        StringBuilder itemsDescription = new StringBuilder();
                        itemsDescription.append("\n\nYou pick up ");

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
                    return "You pick up the " + thing.getPrimaryName() + ".";
                case STATIC:
                    return "It doesn't seem like that can be picked up.";
                case NOTPRESENT:
                    if(player.getInventoryList().contains(thing)) {
                        return "You already have that.";
                    }
                case FAILURE:
                default:
                    if(this.object.equals("all")) {

                    }
                    return "You don't see anything like that nearby.";
            }
        }

        @Override
        public ArrayList<Action> getActions(Player player) {
            ArrayList<Action> actions = new ArrayList<>();
            if(getResult(player) == ItemPresence.SUCCESS) {
                actions.add( (Player p) -> {
                    p.getRoom().getInventoryMixin().remove(thing);
                    p.getInventoryMixin().add(thing);
                });
            } else if(getResult(player) == ItemPresence.ALL) {
                actions.add( (Player p) -> {
                    InventoryMixin<?> roomInvMix = p.getRoom().getInventoryMixin();
                    InventoryMixin<?> playInvMix = p.getInventoryMixin();
                    Item[] inv = roomInvMix.get();
                    for(Item item : inv) {
                        roomInvMix.remove(item);
                        playInvMix.add(item);
                    }
                });
            }
            return actions;
        }
    }

}
