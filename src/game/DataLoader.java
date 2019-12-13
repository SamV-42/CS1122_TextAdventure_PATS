package game;

import world.Direction;
import parser.command.DirectionCommand;

import parser.command.LookCommand;

public class DataLoader {
    public void generateCommands() {

        //Note: once the below instances are made once, we don't need to hold onto references -- Command will statically

        DirectionCommand northCommand = new DirectionCommand("north_command", Direction.NORTH, "north", "n");
        DirectionCommand southCommand = new DirectionCommand("south_command", Direction.SOUTH, "south", "s");
        DirectionCommand eastCommand = new DirectionCommand("east_command", Direction.EAST, "east", "e");
        DirectionCommand westCommand = new DirectionCommand("west_command", Direction.WEST, "west", "w");
        DirectionCommand northeastCommand = new DirectionCommand("northeast_command", Direction.NORTHEAST, "northeast", "north-east", "ne");
        DirectionCommand southeastCommand = new DirectionCommand("southeast_command", Direction.SOUTHEAST, "southeast", "south-east", "se");
        DirectionCommand northwestCommand = new DirectionCommand("northwest_command", Direction.NORTHWEST, "northwest", "north-west", "nw");
        DirectionCommand southwestCommand = new DirectionCommand("southwest_command", Direction.SOUTHWEST, "southwest", "south-west", "sw");
        DirectionCommand upCommand = new DirectionCommand("up_command", Direction.UP, "up", "u");
        DirectionCommand downCommand = new DirectionCommand("down_command", Direction.DOWN, "down", "d");
        DirectionCommand inCommand = new DirectionCommand("in_command", Direction.IN, "in", "i");
        DirectionCommand outCommand = new DirectionCommand("out_command", Direction.OUT, "out", "o");

        LookCommand lookCommand = new LookCommand("look_command", "look", "l", "search", "view");
    }

    public void putRoomBlockers(){
        //Blocker for the cobweb room
        Objection webBlocker = (p, c) -> {
            if(!(c instanceof DirectionCommand)) { return null; }

            DirectionCommand dc = (DirectionCommand)c;

            if( dc.getMixin("id").get() != "north_command") { return null;}

            if(!p.getRoom().getInventoryList().contains(Registration.getOwnerByStr("item_id", "cobwebs"))) { return null; }

            return new Response("The cobwebs seem to be too thick to safely pass through.", 200);
        }
        Registration.getOwnerByStr("room_id", "spider_room").getObjectionMixin()

        Objection gateBlocker = (p,c) -> {
            if(!(c instanceof DirectionCommand)) { return null; }

            DirectionCommand dc = (DirectionCommand)c;

            if( dc.getMixin("id").get() != "north_command") { return null;}

            if( ! p.getRoom().getInventoryList().contains(Registration.getOwnerByStr("item_id", "irongate")) ) { return null; }

            return new Response("The gate is locked", 200){};
        }
    }
}
