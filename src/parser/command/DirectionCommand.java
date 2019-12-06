package parser.command;

import world.Direction;

//Currently relying on the following to be implemented: 
// Room, with "public Room getConnection(Direction dir)" (null if none)
// Player, with "public void getRoom()" and "public void moveTo(Room room)"

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

    private Direction direction;    //Please don't, eg, made a DirectionCommand named NorthCommand and then pass in Direction.SOUTH

    public DirectionCommand(String id, Direction direction, ArrayList<String> names, boolean register) {
        this.direction = direction;

        Response response = new Response("", 100, new Action[1] {}) {

            @Override
            public String getPlayerMessage(Player player) {
                Room target = targetRoom(player);
                if(target != null) {
                    return target.getDescription();     //Print room description on entering room
                } else {
                    return "There doesn't appear to be an exit in that direction.";
                }
            }

            @Override
            public ArrayList<Action> getActions(Player player) {
                Room target = targetRoom(player);
                ArrayList<Action> actions = new ArrayList<Action>();
                if(target != null) {
                    actions.add( () -> {
                        player.moveTo(target);
                    });
                }
                return actions;
            }

            private Room targetRoom(Player player) {
                // We might want to move this functionality to another class
                return player.getRoom().getConnection(direction) //anon. inner class should have access to direction
            }
    }

        super(id, response, names, register);
    }

    public DirectionCommand(String id, Direction direction, ArrayList<String> names) {
        this(id, direction, names, true);
    }

    public DirectionCommand(String id, Direction direction, String... names) {
        this(id, direction, new ArrayList<String>(names.asList()));
    }

    public DirectionCommand(String id, Direction direction, String[] names) {
        this(id, direction, new ArrayList<String>(names.asList()));
    }
}
