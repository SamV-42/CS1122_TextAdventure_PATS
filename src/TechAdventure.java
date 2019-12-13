import game.*;
import parser.*;
import util.*;
import world.*;

import util.mixin.IdMixin;
import util.mixin.NamesMixin;
import util.mixin.InventoryMixin;
import util.mixin.ObjectionMixin;
import parser.command.DirectionCommand;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class TechAdventure {
    public static void main(String[] args) {
        Parser parser = new Parser();

        /* The below couple lines should be read from JSON files in DataReader
            But for now, let's hardcode a testing world */
        Room room1 = new Room("room1");
        Room room2 = new Room("room2");
        Room room3 = new Room("room3");
        Room room4 = new Room("room4");

        room1.setTitle("Waterfall");
        room1.setDescription("A winding trail leads up and past this point. A pretty waterfall descends around you.");
        room1.addConnection(Direction.IN, Registration.getOwnerByStr("room_id", "room2") );
        room1.addConnection(Direction.SOUTHWEST, Registration.getOwnerByStr("room_id", "room3"));
        room1.addConnection(Direction.NORTH, Registration.getOwnerByStr("room_id", "room4"));

        room2.setTitle("Cave");
        room2.setDescription("A small, hidden grotto exists behind the waterfall's curtain. A hidden stair can take you up to the mountain's peak, although you're not sure you'll be able to return the way you came.");
        room2.addConnection(Direction.OUT, Registration.getOwnerByStr("room_id", "room1"));
        room2.addConnection(Direction.UP, Registration.getOwnerByStr("room_id", "room4"));

        room3.setTitle("Trail");
        room3.setDescription("You stand at the bottom of a winding trail. You can hear the rumble of water in the distance.");
        room3.addConnection(Direction.NORTHEAST, Registration.getOwnerByStr("room_id", "room1"));

        room4.setTitle("Mountaintop");
        room4.setDescription("You've reached the peak, or a peak. The view stretches into the distance.");
        room4.addConnection(Direction.SOUTH, Registration.getOwnerByStr("room_id", "room1"));
        room4.addConnection(Direction.UP, Registration.getOwnerByStr("room_id", "room4"));

        Item item1 = new Item("item1", "rusty sword", "sword");
        room1.getInventoryMixin().add(item1);
        Item item2 = new Item("item2", "albatross", "bird");
        room1.getInventoryMixin().add(item2);
        Item item3 = new Item("item3", "water bottle", "bottle", "water");
        item3.setDescription("A bottle of water. Delicious!");
        room1.getInventoryMixin().add(item3);

        Item item4 = new Item("item4", "rocks", "rock", "stone", "stones") {
            @Override
            public String getArticle() { return "some"; }
        };
        room2.getInventoryMixin().add(item4);

        Objection upBlocker = (p, c) -> {
            if(!(c instanceof DirectionCommand)) { return null; }

            DirectionCommand dc = (DirectionCommand)c;

            if( dc.getMixin("id").get() != "up_command") { return null;}

            if( ! p.getRoom().getInventoryList().contains(item4) ) { return null; }

            //So, we're trying to go up from the cave
            return new Response("It looks like some rocks are blocking your way.", 200){};
        };
        room2.getObjectionMixin().add(upBlocker);


        Player player = new Player("1");
        player.setRoom(room3);

        /* I think the below would be handled by all that ServerClient jazz,
            But again, just for testing... */
        System.out.println( parser.runPlayerInput(player, ((Command)(Registration.getOwnerByStr("command_id", "look_command"))).getNames()[0] ) );

        Scanner inputScanner = new Scanner(System.in);
        while(true) {
            System.out.print("> ");
            String input = inputScanner.nextLine();
            System.out.println( parser.runPlayerInput(player, input) );
        }
    }
}
