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

//Currently relying on the following to be implemented:
// Room, with "public Room getConnection(Direction dir)" (null if none)

/*
 *  Represents a command that leads to movement
 *  The specific directions available are defined in DataLoader, along with the other game/language specific data
 *
 *  Date Last Modified: 12/05/19
 *	@author Sam VanderArk, Patrick Philbin, Thomas Grifka, Alex Hromada
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

public class DirectionCommand extends Command {

    private final Direction direction;    //Please don't, eg, made a DirectionCommand named NorthCommand and then pass in Direction.SOUTH

    public DirectionCommand(boolean register, String id, Direction direction, List<String> names) {
        super(register, id, new Response("", 100, new Action[]{}) {

            @Override
            public String getPlayerMessage(Player player) {
                StringBuilder message = new StringBuilder();

                Room target = targetRoom(player);
                if(target != null) {
                    message.append("You go " + direction.getMixin("primaryname").get() + ".\n\n");
                    message.append(target.look());     //Print room description on entering room
                } else {
                    message.append("There doesn't appear to be an exit in that direction.");
                }

                return message.toString();
            }

            @Override
            public ArrayList<Action> getActions(Player player) {
                Room target = targetRoom(player);
                ArrayList<Action> actions = new ArrayList<Action>();
                if(target != null) {
                    actions.add( (p) -> {
                        p.setRoom(target);
                    });
                }
                return actions;
            }

            private Room targetRoom(Player player) {
                // We might want to move this functionality to another class
                return player.getRoom().getConnection(direction); //anon. inner class should have access to direction
            }
        }, names);

        this.direction = direction;
    }

    public DirectionCommand(String id, Direction direction, List<String> names) {
        this(true, id, direction, names);
    }

    public DirectionCommand(String id, Direction direction, String... names) {
        this(id, direction, new ArrayList<String>(Arrays.asList(names)));
    }

}
