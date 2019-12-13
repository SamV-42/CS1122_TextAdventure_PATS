import game.*;
import parser.*;
import util.*;
import world.*;

import util.mixin.IdMixin;
import util.mixin.NamesMixin;
import util.mixin.InventoryMixin;
import util.mixin.ObjectionMixin;
import parser.command.DirectionCommand;

import server.ConnectionEvent;
import server.ConnectionListener;
import server.UnknownConnectionException;
import server.AdventureServer;

import java.util.Scanner;
import java.util.ArrayList;

import java.io.IOException;

public class TechAdventure implements ConnectionListener {

    AdventureServer adventureServer = null;
    ArrayList<Player> playerList = null;
    Parser parser = null;
    Room room1 = null;
    Room room2 = null;
    Room room3 = null;
    Room room4 = null;

    public TechAdventure(){
        playerList = new ArrayList<>();
        adventureServer = new AdventureServer();
        adventureServer.setOnTransmission(this);
        try {
            initilize();
        }catch(UnknownConnectionException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 2112;
        if(args.length > 0 && args[0] != null){
            port = Integer.parseInt(args[0]);
        }
        TechAdventure techAdventure = new TechAdventure();
        techAdventure.start(port);
    }

    @Override
    public void handle ( ConnectionEvent e ) {
        System.out.println( String.format ( "connectionId=%d, data=%s", e.getConnectionID (), e.getData() ));
        try {
            switch ( e.getCode ( ) ) {
                case CONNECTION_ESTABLISHED:
                    Player newPlayer = new Player(""+e.getConnectionID (),e.getConnectionID ());
                    if(playerList.size() == 0){
                        newPlayer.setHost(true);
                    }
                    playerList.add(newPlayer);
                    newPlayer.setRoom(room3);
                    break;
                case TRANSMISSION_RECEIVED:
                    Player player = null;
                    for ( Player existPlayer : playerList) {
                        if(existPlayer.getConnectionID () == e.getConnectionID ()){
                            player = existPlayer;
                            break;
                        }
                    }
                    if ( e.getData ( ).equals ( "SHUTDOWN" ) && player.isHost()) {
                        for (Player existPlayer: playerList) {
                            adventureServer.sendMessage ( existPlayer.getConnectionID ( ), "CONNECTION TERMINATED: REASON HOST DISCONNECT");
                            try{
                                adventureServer.disconnect(existPlayer.getConnectionID());
                            }catch(IOException error){
                                error.printStackTrace();
                            }
                        }
                        adventureServer.stopServer ( );
                    }
                    if( e.getData().equals("QUIT")){
                        try {
                            adventureServer.disconnect(e.getConnectionID());
                        }catch(IOException error){
                            error.printStackTrace();
                        }
                    }
                    adventureServer.sendMessage ( e.getConnectionID ( ), parser.runPlayerInput(player, e.getData()) );
                    break;
                case CONNECTION_TERMINATED:
                    for (int i = 0; i < playerList.size(); i++){
                        if(playerList.get(i).getConnectionID() == e.getConnectionID()){
                            if(playerList.get(i)!=null) {
                                parser.runPlayerInput(playerList.get(i), "drop all");
                            }
                            if (playerList.get(i) !=null && playerList.get(i).isHost()) {
                                adventureServer.stopServer ( );
                                break;
                            }
                            playerList.remove(i);
                        }
                    }
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

    public void initilize() throws UnknownConnectionException {
        /* The below couple lines should be read from JSON files in DataReader
            But for now, let's hardcode a testing world */
        room1 = new Room("1");
        room2 = new Room("2");
        room3 = new Room("3");
        room4 = new Room("4");

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

        parser = new Parser();
    }
}
