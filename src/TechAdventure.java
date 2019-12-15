import game.*;
import parser.*;
import util.*;
import world.*;

import util.mixin.IdMixin;
import util.mixin.NamesMixin;
import util.mixin.InventoryMixin;
import util.mixin.ObjectionMixin;
import parser.command.DirectionCommand;
import server.*;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.IOException;

public class TechAdventure implements ConnectionListener {

    private DataLoader loader = null;
    private AdventureServer adventureServer = null;
    private ArrayList<Player> playerList = null;
    private Parser parser = null;
    private AnotherLoader anotherLoader = null;
    private boolean stopping;
    private Minotaur mino = null;

    public TechAdventure(){
        anotherLoader = new AnotherLoader();
        playerList = new ArrayList<>();
        stopping = false;
        loader = new DataLoader();
        adventureServer = new AdventureServer();
        adventureServer.setOnTransmission(this);
        try {
            initialize();
        }catch(UnknownConnectionException e){
            e.printStackTrace();
        }
        mino = new Minotaur(Registration.<Room>getOwnerByStr("room_id", "min_den"));
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
                                adventureServer.sendMessage(e.getConnectionID(), existPlayer.getId() + " has been assigned to you");
                                break;
                            }
                        }
                        if (!found) {
                            adventureServer.sendMessage(e.getConnectionID(), "Unable to find a charactor with name: " + input.substring(9));
                        }
                    }else if(input.length() > 4 && input.substring(0,3).equals("new") && player == null) {
                        Player newPlayer = new Player(input.substring(4), e.getConnectionID());
                        newPlayer.setRoom(Registration.getOwnerByStr("room_id", "entrance"));
                        playerList.add(newPlayer);
                        adventureServer.sendMessage(e.getConnectionID(), "New Player created: " + newPlayer.getId());
                    } else if(player != null && input.length() > 4 && input.substring(0,3).equals("say")) {
                        for (Player existPlayer : playerList) {
                            if (!existPlayer.equals(player)) {
                                adventureServer.sendMessage(existPlayer.getConnectionID(), player.getId() + " says: " +
                                        e.getData().substring(4));
                            }
                        }
                    } else if(player != null && input.equals("wait") || input.equals("getMessages")){
                            //allows the client to get new messeges
                    } else if (player != null && input.equals ( "shutdown" ) && player.isHost()) {
                        stop();
                        stopping = true;
                    } else if( input.equals("quit") && player != null) {
                        try {
                            if (player.isHost()) {
                                stop();
                                stopping = true;
                            } else {
                                adventureServer.disconnect(e.getConnectionID());
                            }
                        } catch (IOException error) {
                            error.printStackTrace();
                        }
                    } else if (player != null && e.getData().length() > 3 && e.getData().substring(0,2).equals("go")){
                        Room room = player.getRoom();
                        if(room.getId().substring(0,3).equals("lab") || room.getId().substring(0,3).equals("axe") || room.getId().substring(0,3).equals("min")
                                || room.getId().equals("tunnel")){
                            mino.move();
                        }
                        adventureServer.sendMessage(e.getConnectionID(), parser.runPlayerInput(player, input));
                        if(mino.getRoom().equals(player.getRoom())){
                            player.kill();
                            System.out.println(player + " died to the Minotaur");
                        }
                    } else if(player != null){
                        adventureServer.sendMessage(e.getConnectionID(), parser.runPlayerInput(player, input));
                    } else{
                        adventureServer.sendMessage(e.getConnectionID(), "You have not created a charector or taken over" +
                                " one");
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

    public void initialize() throws UnknownConnectionException {

        anotherLoader.loadStuff();

        parser = new Parser();
    }

    public Minotaur getMino() {
        return mino;
    }
}
