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

    DataLoader loader = null;
    AdventureServer adventureServer = null;
    ArrayList<Player> playerList = null;
    Parser parser = null;
    boolean stopping;
    Room room1 = null;
    Room room2 = null;
    Room room3 = null;
    Room room4 = null;

    public TechAdventure(){
        playerList = new ArrayList<>();
        stopping = false;
        loader = new DataLoader();
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
        String input = e.getData().toLowerCase();
        try {
            switch ( e.getCode ( ) ) {
                case CONNECTION_ESTABLISHED:
                    //adventureServer.sendMessage(e.getConnectionID(), "Please enter either \"EXISTING (NAME OF CHARACTOR)\"" +
                    //" or \"NEW (NAME OF NEW CHARACTOR\")\n Otherwise you will not be able to do ANYTHING");
                    break;
                case TRANSMISSION_RECEIVED:
                    Player player = null;
                    for ( Player existPlayer : playerList) {
                        if(existPlayer.getConnectionID () == e.getConnectionID ()){
                            player = existPlayer;
                            break;
                        }
                    }
                    if(input.length() > 9 && input.substring(0,8).equals("existing") && player == null) {
                        boolean found = false;
                        for (Player existPlayer : playerList) {
                            if (existPlayer.getId().equals(input.substring(9))) {
                                existPlayer.setConnectionID(e.getConnectionID());
                                found = true;
                                System.out.println(existPlayer.getId() + " has been assigned to Connection: " + e.getConnectionID());
                                adventureServer.sendMessage(e.getConnectionID(), existPlayer.getId() + "has been assigned to you");
                                break;
                            }
                        }
                        if (!found) {
                            adventureServer.sendMessage(e.getConnectionID(), "Unable to find a charactor with name: " + input.substring(9));
                        }
                    }else if(input.length() > 4 && input.substring(0,3).equals("new") && player == null){
                        Player newPlayer = new Player(input.substring(4), e.getConnectionID());
                        newPlayer.setRoom(room3);
                        playerList.add(newPlayer);
                        adventureServer.sendMessage(e.getConnectionID(), "New Player created: " + newPlayer.getId());
                    } else if ( input.equals ( "shutdown" ) && player.isHost()) {
                        stop();
                        stopping = true;
                    } else if( input.equals("quit")){
                        try {
                            if(player.isHost()){
                                stop();
                                stopping = true;
                            }else {
                                adventureServer.disconnect(e.getConnectionID());
                            }
                        }catch(IOException error){
                            error.printStackTrace();
                        }
                    } else {
                        adventureServer.sendMessage(e.getConnectionID(), parser.runPlayerInput(player, input));
                    }
                    break;
                case CONNECTION_TERMINATED:
                    System.out.println("Player Discconected: " +e.getConnectionID() );
                    break;
                default:
                    break;
            }
        } catch ( UnknownConnectionException unknownConnectionException ) {
            unknownConnectionException.printStackTrace ( );
        }
    }

    public void start( int port ) {
        adventureServer.startServer ( port );
    }

    public void stop() throws UnknownConnectionException {
        for (Player existPlayer: playerList) {
            if(existPlayer !=null) {
                parser.runPlayerInput(existPlayer, "drop all");
            }
            if(!existPlayer.isHost()){
                adventureServer.sendMessage ( existPlayer.getConnectionID ( ), "CONNECTION TERMINATED: REASON HOST DISCONNECT");
            }else{
                adventureServer.sendMessage ( existPlayer.getConnectionID(), "DISCONNECTED");
            }
            try{
                adventureServer.disconnect(existPlayer.getConnectionID());
            }catch(IOException error){
                error.printStackTrace();
            }
        }
        adventureServer.stopServer ( );
        playerList = null;
        System.out.println("Server Stopped");
    }

    public void initilize() throws UnknownConnectionException {

        loader.generateCommands();

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