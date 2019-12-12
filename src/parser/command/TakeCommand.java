package parser.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import parser.*;
import util.*;
import util.mixin.*;
import world.*;

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


    private class TakeResponse extends Response {
        public TakeResponse(int severity) {
            super("", severity);
        }

        private Item thing = null;

        protected void initialize(String object) {
            thing = Registration.searchOwnerByStr("item_name", object);
        }

        private boolean getSuccess(Player player) {
            return ( thing != null && player.getRoom().<InventoryMixin>getTypeMixin("inventory").itemPresent(thing) );
        }

        @Override
        public String getPlayerMessage(Player player) {
            return getSuccess(player)
                ? "You pick up the " + thing.<PrimaryNameMixin>getTypeMixin("primaryname").get() + "."
                : "You don't see anything like that nearby.";
                //todo: add "you already have that", "that doesn't look like you can pick it up", and "that's not something you can pick up"
        }

        @Override
        public ArrayList<Action> getActions(Player player) {
            ArrayList<Action> actions = new ArrayList<>();
            if(getSuccess(player)) {
                actions.add( (Player p) -> {
                    p.getRoom().<InventoryMixin>getTypeMixin("inventory").remove(thing);
                    p.<InventoryMixin>getTypeMixin("inventory").add(thing);
                });
            }
            return actions;
        }
    }

}
