package game;

import parser.*;
import parser.command.*;
import util.Registration;
import world.*;

import java.util.ArrayList;

/**
 * Class to load all the commands and events required to make the game function
 *
 * @author Patrick Philbin, Thomas Grifka, Alex Hromada, Sam VanderArk
 * Date Last Modified: 12/17/19
 * CS 1122 L02
 */

public class DataLoader {

    /**
     * Generates the commands neccessary to do things in the game.
     */
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
                try {
                    Command command = (Registration.<Command>searchOwnerByStr("command_name", replacement)).get(0);
                    return (command instanceof DirectionCommand);
                } catch(java.lang.IndexOutOfBoundsException e) { return false; }
            }
        };

        LookCommand lookCommand = new LookCommand("look_command", "look", "l", "search", "view");

        TakeCommand takeCommand = new TakeCommand("take_command", "take", "get", "pick up", "seize", "snatch", "grab", "yoink", "nab");

        DropCommand dropCommand = new DropCommand("drop_command", "drop", "throw", "yeet");

        ExamineCommand examineCommand = new ExamineCommand("examine_command", "examine", "x");

        UseCommand useCommand = new UseCommand("use_command", "use");

        InventoryCommand invCommand = new InventoryCommand("inventory_command", "inventory", "inv");
    }

    /**
     * A loader method to initialize events and blockers.
     */
    public void putRoomBlockers(Parser parser){
        //Spider kills you if you try to walk through the webs
        Objection webBlocker = (p, c, cR) -> {

            if(!(c instanceof DirectionCommand)) { return null; }
            DirectionCommand dc = (DirectionCommand)c;

            if(dc.getMixin("id").get() != "north_command") { return null; }

            if( (p.getRoom().getInventoryList().contains(Registration.getOwnerByStr("item_id", "cobwebs")))) {
                return new Response("As you try to push through the webs, you are suddenly bitten by the massive spider!" +
                    " You feel it's venom seep into your veins as you collapse. You are dead.",
                    200, (play) -> { play.kill();} );
            } else {
                return new Response("\nAs you walk through the doors, the spider hisses at you and blocks the entrance with new webs. "
                + "That's kind of horrifying, actually." + cR[0].getPlayerMessage(p), 200, (play) -> {cR[0].run(play);});
            }
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
                        return "You unlock the gate and pass through it. It swings closed behind you with a loud, ominous *CRUNCH*.\n" + cR[0].getPlayerMessage(p);
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

        Objection useKey = (p,c,cR) -> {
            if(! (c instanceof UseCommand)) { return null; }
            UseCommand uc = (UseCommand)c;
            if(! Registration.getOwnerByStr("item_id", "key").equals(uc.getUsedItem())) { return null; }
            return new Response("You should be able to walk through the gate as long as you've got the key.", 200);
        };
        Registration.<Room>getOwnerByStr("room_id", "dungeon_hall_2").getObjectionMixin().add(useKey);



        //--------------------------------------------------------------------------------------------------------------

        //Event to replace the torch with the lit torch
        Objection replaceTorch = (p,c,cR) -> {
            if(!(c instanceof UseCommand)) { return null; }

            UseCommand uc = (UseCommand)c;

            if(! Registration.getOwnerByStr("item_id", "torch").equals(uc.getUsedItem())) { return null; }
            if(!p.getInventoryList().contains(uc.getUsedItem())) { return null; }

            return new Response("The torch is set ablaze by the brazier!",
            200, (play) -> {
                play.getInventoryMixin().remove(Registration.getOwnerByStr("item_id", "torch"));
                play.getInventoryMixin().add(Registration.getOwnerByStr("item_id", "littorch"));
            });
        };
        Registration.<Room>getOwnerByStr("room_id", "chapel").getObjectionMixin().add(replaceTorch);
        //--------------------------------------------------------------------------------------------------------------

        //Event to burn the cobwebs
        Objection burnWebs = (p,c,cR) -> {
            if(!(c instanceof UseCommand)) { return null; }

            UseCommand uc = (UseCommand)c;

            if(! Registration.getOwnerByStr("item_id", "littorch").equals(uc.getUsedItem())) { return null; }
            if(!p.getInventoryList().contains(uc.getUsedItem())) { return null; }

            return new Response("The cobwebs burn away before your torch!",
            200, (play) -> {play.getRoom().getInventoryMixin().remove(Registration.getOwnerByStr("item_id", "cobwebs")); });
        };
        Registration.<Room>getOwnerByStr("room_id", "spider_room").getObjectionMixin().add(burnWebs);

        //Event to "reveal" the key
        Objection keyReveal = (p,c, cR) -> {
            if(!(c instanceof ExamineCommand)) { return null; }

            ExamineCommand ec = (ExamineCommand)c;

            if(!ec.getExamined().equals(Registration.getOwnerByStr("item_id", "skeleton"))) { return null; }
            if(!p.getRoom().getInventoryList().contains(Registration.getOwnerByStr("item_id", "key"))) { return null; }

            return new Response(cR[0].getPlayerMessage(p) + "\n...Hey, you hadn't noticed that key before!", 200, (play) -> { Registration.<Item>getOwnerByStr("item_id", "key").setHidden(false); });
        };
        Registration.<Room>getOwnerByStr("room_id", "cell").getObjectionMixin().add(keyReveal);


        //--------------------------------------------------------------------------------------------------------------
        Objection minotaurKiller = (p,c,cR) -> {
            Response badOutcome = new Response("The minotaur charges you! You have died.", 300, (play) -> { play.kill(); });
            // we'll check any command, because the minotaur moves on its own
            Room room = p.getRoom();
            Minotaur mino = parser.getMinotaur();
            if(room.equals(Registration.<Room>getOwnerByStr("room_id", "cutscene"))) {
                if(c instanceof UseCommand) {
                    UseCommand uc = ((UseCommand)c);
                    if(uc.getUsedItem().equals(Registration.<Item>getOwnerByStr("item_id", "golden_axe"))) {
                        return new Response("You swing the golden axe with virtuous force! The minotaur topples over, defeated.\n\n***** YOU HAVE WON *****", 150,
                        (play) -> {
                            mino.setRoom(Registration.<Room>getOwnerByStr("room_id", "min_den"));
                            Registration.<Room>getOwnerByStr("room_id", "axe_room").getInventoryMixin().add(Registration.<Item>getOwnerByStr("item_id", "golden_axe"));
                            //DISCNNECT THE PLAYER SOMEHOW!
                        } );
                    }
                }
                return badOutcome;
            }

            //if it's not the cutscene, just kill them -- they won't have the axe, barring maybe save/load shenanigans
            if (room.getInventoryList().contains(mino)) {
                return badOutcome;
            }
            return null;
            /*try {
                adventureServer.disconnect(e.getConnectionID());
            } catch (IOException minoE) {
                System.out.println("Unable to discconnect: " + e.getConnectionID());
            }*/
        };
        parser.getObjectionMixin().add(minotaurKiller);

    }
}
