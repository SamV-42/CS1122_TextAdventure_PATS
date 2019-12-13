import game.*;
import parser.*;
import util.*;
import world.*;

import util.ConnectionEvent;
import util.ConnectionListener;
import util.UnknownConnectionException;

import java.util.Scanner;

public class TechAdventure implements ConnectionListener {

    AdventureServer adventureServer = null;
    Player player = null;
    Parser parser = null;
    Room room1 = null;
    Room room2 = null;
    Room room3 = null;
    Room room4 = null;

    public TechAdventure(){
        adventureServer = new AdventureServer();
        adventureServer.setOnTransmission(this);
    }

    public static void main(String[] args) {
        int port = 2112;
        if(args.length > 0 && args[0] != null){
            port = Integer.parseInt(args[0]);
        }
        TechAdventure techAdventure = new TechAdventure();
        techAdventure.start(port);





        /* I think the below would be handled by all that ServerClient jazz,
            But again, just for testing... */
        /*
        System.out.println( parser.runPlayerInput(player, Command.getCommandById("look_command").getNames()[0]) );

        Scanner inputScanner = new Scanner(System.in);
        while(true) {
            System.out.print("> ");
            String input = inputScanner.nextLine();
            System.out.println( parser.runPlayerInput(player, input) );
        }

         */
    }
    @Override
    public void handle ( ConnectionEvent e ) {
        System.out.println( "EVENT RECEIVED - YOU MUST PARSE THE DATA AND RESPOND APPROPRIATELY");
        System.out.println( String.format ( "connectionId=%d, data=%s", e.getConnectionID (), e.getData() ));
        try {
            switch ( e.getCode ( ) ) {
                case CONNECTION_ESTABLISHED:
                    initilize( e );
                    break;
                case TRANSMISSION_RECEIVED:
                    adventureServer.sendMessage ( e.getConnectionID ( ), parser.runPlayerInput(player, e.getData()) );
                    // BEWARE - if you keep this, any user can shutdown the server
                    if ( e.getData ( ).equals ( "SHUTDOWN" ) ) {
                        adventureServer.stopServer ( );
                    }
                    break;
                case CONNECTION_TERMINATED:
                    // Cleanup when the connection is terminated.
                    break;
                default:
                    // What is a reasonable default?
            }
        } catch ( UnknownConnectionException unknownConnectionException ) {
            unknownConnectionException.printStackTrace ( );
        }
    }

    public void start( int port ) {
        adventureServer.startServer ( port );
    }

    public void initilize(ConnectionEvent e)throws UnknownConnectionException{
        /* The below couple lines should be read from JSON files in DataReader
            But for now, let's hardcode a testing world */
        room1 = new Room();
        room2 = new Room();
        room3 = new Room();
        room4 = new Room();

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

        player = new Player();
        player.setRoom(room3);
        parser = new Parser();
        adventureServer.sendMessage ( e.getConnectionID ( ), parser.runPlayerInput(player, Command.getCommandById("look_command").getNames()[0]) );
    }
}
