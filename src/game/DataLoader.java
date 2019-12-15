package game;

import util.*;
import util.mixin.*;
import util.Registration;
import world.*;
import parser.*;
import parser.command.*;

import java.util.ArrayList;


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

        Command goCommand = new Command("go_command", new Response("I don't recognize that direction.", 500), "go") {
            @Override
            public boolean isReplace(String playerInput) {
                String replacement = this.replacementText(playerInput);
                Command command = (Registration.<Command>searchOwnerByStr("command_name", replacement)).get(0);
                return (command instanceof DirectionCommand);
            }
        };

        LookCommand lookCommand = new LookCommand("look_command", "look", "l", "search", "view");

        TakeCommand takeCommand = new TakeCommand("take_command", "take", "get", "pick up", "seize", "snatch", "grab", "yoink", "nab");

        DropCommand dropCommand = new DropCommand("drop_command", "drop", "throw", "yeet");

        ExamineCommand examineCommand = new ExamineCommand("examine_command", "examine", "x");

        UseCommand useCommand = new UseCommand("use_command", "use");

        InventoryCommand invCommand = new InventoryCommand("inventory_command", "inventory", "inv");
    }

    public void putRoomBlockers() {
        //Blocker for the cobweb room
        Objection webBlocker = (p, c, cR) -> {
            if(!(c instanceof DirectionCommand)) { return null; }

            DirectionCommand dc = (DirectionCommand)c;

            if( dc.getMixin("id").get() != "north_command") { return null; }

            if(p.getRoom().getInventoryMixin().contains(Registration.getOwnerByStr("item_id", "cobwebs"))) { return null; }

            //p.kill();
            return new Response("As you try to push through the webs, you are suddenly bitten by a massive spider!" +
                    "You feel it's venom seep into your veins as you collapse. You are dead.", 200);
        };

        Registration.<Room>getOwnerByStr("room_id", "spider_room").getObjectionMixin().add(webBlocker);


        Objection gateBlocker = (p,c,cR) -> {
            if(!(c instanceof DirectionCommand)) { return null; }

            DirectionCommand dc = (DirectionCommand)c;

            if( dc.getMixin("id").get() != "north_command") { return null;}

            if( ! p.getRoom().getInventoryList().contains(Registration.getOwnerByStr("item_id", "irongate")) ) { return null; }

            return new Response("", 101) {
                @Override
                public String getPlayerMessage(Player player) {

                    if( checkPlayerHasKey(player) ) {
                        return "You unlock the gate and pass through it. It closes behind you.\n" + cR[0].getPlayerMessage(p);
                    }
                    return "The gate is locked.";
                }

                @Override
                public ArrayList<Action> getActions(Player player) {
                    ArrayList<Action> acts = new ArrayList<>();
                    if( checkPlayerHasKey(player) ) {
                        acts.addAll( cR[0].getActions(p) );
                    }
                    return acts;
                }

                private boolean checkPlayerHasKey(Player player) {
                    return player.getInventoryList().contains( Registration.getOwnerByStr("item_id", "key") );
                }
            };
        };
        Registration.<Room>getOwnerByStr("room_id", "dungeon_hall_2").getObjectionMixin().add(gateBlocker);
    }
}
