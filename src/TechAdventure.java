import game.*;
import parser.*;
import util.*;
import world.*;

import parser.command.DirectionCommand;


import game.CustomRoomSample;
import world.Room;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


import java.util.Scanner;

public class TechAdventure {
    public static void main(String[] args) throws Exception {

        try {
            String customClassName = "CustomRoomSample";
            Class<?> customClass = Class.forName("game." + customClassName);
            Constructor<?> ctor = customClass.getConstructor(String.class);
            Room customRoom = (Room)(ctor.newInstance(new Object[]{ "custom_id" }));
            customRoom.setTitle("Yuh");
            customRoom.setDescription("Yuuuuuuuuuh");
            System.out.println(customRoom.getTitle());
            System.out.println(customRoom.getDescription());
        } catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new Exception(e);
        }


        Parser parser = new Parser();

        /* The below couple lines should be read from JSON files in DataReader
            But for now, let's hardcode a testing world */
        Room room1 = new Room("room1");
        Room room2 = new Room("room2");
        Room room3 = new Room("room3");
        Room room4 = new Room("room4");

        room1.setTitle("Waterfall");
        room1.setDescription("A winding trail leads up and past this point. A pretty waterfall descends around you.");
        room1.addConnection(Direction.IN, room2);
        room1.addConnection(Direction.SOUTHWEST, room3);
        room1.addConnection(Direction.NORTH, room4);

        room2.setTitle("Cave");
        room2.setDescription("A small, hidden grotto exists behind the waterfall's curtain. A hidden stair can take you up to the mountain's peak, although you're not sure you'll be able to return the way you came.");
        room2.addConnection(Direction.OUT, room1);
        room2.addConnection(Direction.UP, room4);

        room3.setTitle("Trail");
        room3.setDescription("You stand at the bottom of a winding trail. You can hear the rumble of water in the distance.");
        room3.addConnection(Direction.NORTHEAST, room1);

        room4.setTitle("Mountaintop");
        room4.setDescription("You've reached the peak, or a peak. The view stretches into the distance.");
        room4.addConnection(Direction.SOUTH, room1);


        Objection upBlocker = (p, c) -> {
            if(!(c instanceof DirectionCommand)) { return null; }

            DirectionCommand dc = (DirectionCommand)c;

            if( dc.getId() != "up_command") { return null;}

            //So, we're trying to go up from the cave
            return new Response("It looks like some rocks are blocking your way.", 200){};
        };
        room2.getObjectionComponent().addObjection(upBlocker);


        Player player = new Player();
        player.setRoom(room3);

        /* I think the below would be handled by all that ServerClient jazz,
            But again, just for testing... */
        System.out.println( parser.runPlayerInput(player, Command.getCommandById("look_command").getNames()[0]) );

        Scanner inputScanner = new Scanner(System.in);
        while(true) {
            System.out.print("> ");
            String input = inputScanner.nextLine();
            System.out.println( parser.runPlayerInput(player, input) );
        }
    }
}
