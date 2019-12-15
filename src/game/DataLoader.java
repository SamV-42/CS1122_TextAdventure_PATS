package game;

import util.Registration;
import world.*;
import parser.*;
import parser.command.*;


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
                Command command = Registration.searchOwnerByStr("command_name", replacement);
                return (command instanceof DirectionCommand);
            }
        };

        LookCommand lookCommand = new LookCommand("look_command", "look", "l", "search", "view");

        TakeCommand takeCommand = new TakeCommand("take_command", "take", "get", "pick up", "seize", "snatch", "grab", "yoink", "nab");

        DropCommand dropCommand = new DropCommand("drop_command", "drop", "throw", "yeet");

        ExamineCommand examineCommand = new ExamineCommand("examine_command", "examine", "x");

        UseCommand useCommand = new UseCommand("use_command", "use");
    }

    public void putRoomBlockers(){
        //Spider kills you if you try to walk through the webs
        Objection webBlocker = (p, c) -> {
            if(!(c instanceof DirectionCommand)) { return null; }

            DirectionCommand dc = (DirectionCommand)c;
            Player play = (Player)p;

            if(dc.getMixin("id").get() != "north_command") { return null; }

            if(!play.getRoom().getInventoryMixin().itemPresent(Registration.getOwnerByStr("item_id", "cobwebs"))) { return null; }

            //p.kill();
            return new Response("As you try to push through the webs, you are suddenly bitten by a massive spider!" +
                    "You feel it's venom seep into your veins as you collapse. You are dead.", 200);
        };
        Registration.<Room>getOwnerByStr("room_id", "spider_room").getObjectionMixin().add(webBlocker);
        //--------------------------------------------------------------------------------------------------------------

        //Blocker for the gate
        Objection gateBlocker = (p,c) -> {
            if(!(c instanceof DirectionCommand)) { return null; }

            DirectionCommand dc = (DirectionCommand)c;

            if( dc.getMixin("id").get() != "north_command") { return null;}

            return new Response("The gate is locked", 200){};
        };
        Registration.<Room>getOwnerByStr("room_id", "dungeon_hall_2").getObjectionMixin().add(gateBlocker);
        //--------------------------------------------------------------------------------------------------------------

        //Event to replace the torch with the lit torch
        Objection replaceTorch = (p,c) -> {
            if(!(c instanceof UseCommand)) { return null; }

            UseCommand uc = (UseCommand)c;
            Player play = (Player)p;

            if(!uc.getUsedItem().equals(Registration.getOwnerByStr("item_id", "torch"))) { return null; }

            play.getInventoryMixin().remove(Registration.getOwnerByStr("item_id", "torch"));
            play.getInventoryMixin().add(Registration.getOwnerByStr("item_id", "littorch"));
            return new Response("The torch is set ablaze by the brazier!", 200);
        };
        Registration.<Room>getOwnerByStr("room_id", "chapel").getObjectionMixin().add(replaceTorch);
        //--------------------------------------------------------------------------------------------------------------

        //Event to burn the cobwebs
        Objection burnWebs = (p,c) -> {
            if(!(c instanceof UseCommand)) { return null; }

            UseCommand uc = (UseCommand)c;
            Player play = (Player)p;

            if(!uc.getUsedItem().equals(Registration.getOwnerByStr("item_id", "littorch"))) { return null; }

            play.getRoom().getInventoryMixin.remove(Registration.getOwnerByStr("item_id", "cobwebs"));

            return new Response("The cobwebs burn away before your torch!", 200);
        };
        Registration.<Room>getOwnerByStr("room_id", "spider_room").getObjectionMixin().add(burnWebs);
        //--------------------------------------------------------------------------------------------------------------

        //Event to "reveal" the key
        Objection keyReveal = (p,c) -> {
            if(!(c instanceof ExamineCommand)) { return null; }

            ExamineCommand ec = (ExamineCommand)c;
            Player play = (Player)p;

            if(!ec.getExamined().equals(Registration.getOwnerByStr("item_id", "skeleton"))) { return null; }

            play.getRoom().getInventoryMixin().add(Registration.getOwnerByStr("item_id", "key"));

            return new Response("", 200);
        };

        //Event to unlock the gate
        Objection unlockGate = (p,c) -> {
            if(!(c instanceof UseCommand)) { return null; }

            UseCommand uc = (UseCommand)c;
            Player play = (Player)p;

            if(!uc.getUsedItem().equals(Registration.getOwnerByStr("item_id", "key"))) { return null; }

            play.getInventoryMixin().remove(Registration.getOwnerByStr("item_id", "key"));
            play.setRoom(Registration.<Room>getOwnerByStr("room_id", "labyrinth_enter"));
            return new Response("The gate unlocks! But as you step through to the next room, it swings closed and the key falls to the ground!");
        };
        Registration.<Room>getOwnerByStr("room_id", "dungeon_hall_2");
    }
}