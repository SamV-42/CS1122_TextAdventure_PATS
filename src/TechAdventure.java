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
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.HashMap;
import java.lang.StringBuilder;

/**
 * The text adventure game
 * <p>
 * Date Last Modified: 12/18/2019
 *
 * @author Thomas Grifka, Sam VanderArk, Patrick Philbin, Alex Hromada
 * <p>
 * CS1122, Fall 2019
 * Lab Section 2
 */

public class TechAdventure implements ConnectionListener {

    private AdventureServer adventureServer = null; //the server the players will connect to
    private ArrayList<Player> playerList = null; // the list of currently connected players
    private Parser parser = null; //the command parser used
    private AnotherLoader anotherLoader = null; // the loader to load data
    private boolean stopping; //whereter or not the server is stopping, controlls how discconects work
    boolean started; //wherter or not a world has actually been loaded
    private Minotaur mino = null; //the minotaur of the world, ie boss
    private HashMap<String, String> saves = new HashMap<>();  //a map of saves and their names

    /**
     * the constructor of the TechAdventure
     */
    public TechAdventure() {
        anotherLoader = new AnotherLoader();
        playerList = new ArrayList<>();
        stopping = false;
        started = false;
        adventureServer = new AdventureServer();
        adventureServer.setOnTransmission(this);
    }

    /**
     * the main method
     *
     * @param args gets the port number
     */
    public static void main(String[] args) {
        int port = 2112;
        if (args.length > 0 && args[0] != null) {
            port = Integer.parseInt(args[0]);
        }
        TechAdventure techAdventure = new TechAdventure();
        techAdventure.start(port);
    }

    /**
     * handles connection events
     *
     * @param e the connection event
     */
    @Override
    public void handle(ConnectionEvent e) {
        System.out.println(String.format("connectionId=%d, data=%s", e.getConnectionID(), e.getData()));
        String input = e.getData().toLowerCase().trim();
        try {
            switch (e.getCode()) {
                case CONNECTION_ESTABLISHED:
                    //Nothing needs to be done with how we have configured the client and server
                    break;
                case TRANSMISSION_RECEIVED:
                    //determines if the user has an established charactor
                    Player player = null;
                    for (Player existPlayer : playerList) {
                        if (existPlayer.getConnectionID() == e.getConnectionID()) {
                            player = existPlayer;
                            break;
                        }
                    }
                    if (player != null && player.isDead()) {
                        adventureServer.sendMessage(e.getConnectionID(), "You have died and therefore have been " +
                                "disconnected from the server");
                        playerList.remove(player);
                        try {
                            adventureServer.disconnect(e.getConnectionID());
                        } catch (IOException eIO) {
                            System.out.println("ERROR: Disconnecting dead player");
                        }
                    }
                    if(player != null && player.hasWon()){
                        for (int x = 0; x < playerList.size(); x++) {
                            Player temp = playerList.get(x);
                            adventureServer.sendMessage(temp.getConnectionID(), player.getId() + " has killed the minotaur!! You all" +
                                    " you all have won!! \n You will now be discconected from the server");
                            try {
                                adventureServer.disconnect(temp.getConnectionID());
                            }catch(IOException ioe){
                                System.out.println("ERROR: Cannot discconect player after win");
                            }

                        }
                        adventureServer.stopServer();
                    }
                    //forces the user to either load or start a new game
                    if (input.length() >= 8 && started == false && input.substring(0, 8).equals("new-game")) {
                        started = true;
                        initialize();
                        adventureServer.sendMessage(e.getConnectionID(), "NEW Game has finished loading, please procced " +
                                "by making a charactor with \"new (NAME OF CHARACTOR)\"");
                    } else if (started == true) {
                        //once a game has been loaded this is the only section to run

                        //handles assigning an existing charactor to the user
                        if (input.length() > 9 && input.substring(0, 8).equals("existing") && player == null) {
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
                            //handles assigning a new charactor to the user
                        } else if (input.length() > 4 && input.substring(0, 3).equals("new") && player == null) {
                            Player newPlayer = new Player(input.substring(4), e.getConnectionID());
                            newPlayer.setRoom(Registration.getOwnerByStr("room_id", "entrance"));
                            playerList.add(newPlayer);
                            adventureServer.sendMessage(e.getConnectionID(), "New Player created: " + newPlayer.getId());
                            //the say command is handled here
                        } else if (player != null && input.length() > 4 && input.substring(0, 3).equals("say")) {
                            for (Player existPlayer : playerList) {
                                if (!existPlayer.equals(player)) {
                                    adventureServer.sendMessage(existPlayer.getConnectionID(), player.getId() + " says: " +
                                            e.getData().substring(4));
                                }
                            }
                        } else if (player != null && input.length() > 3 && input.substring(0,2).equals("go") &&
                                player.inLab()) {
                            parser.runPlayerInput(player, input);
                            parser.getMinotaur().move();
                            System.out.println(parser.getMinotaur().getRoom().getTitle());
                            if (parser.getMinotaur().getRoom().equals(player.getRoom())) {
                                adventureServer.sendMessage(e.getConnectionID(), "You have moved into the next room " +
                                        "just to find the Minotaur racing towards you, it strike you square in the chest" +
                                        "and you die.");
                                player.kill();
                            } else {
                                adventureServer.sendMessage(e.getConnectionID(), parser.runPlayerInput(player, "look"));
                            }

                            //the wait command handled here
                        } else if (player != null && input.equals("wait") || input.equals("getMessages")) {
                            //allows the client to get new messeges
                            //shutdown command handled
                        } else if (player != null && input.equals("shutdown") && player.isHost()) {
                            stop();
                            stopping = true;
                            //handles a player wanting to quit
                        } else if (input.equals("quit") && player != null) {
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

                        } else if (input.length() > 5 && input.substring(0, 5).equals("save ")) {

                            String target = input.substring(5);

                            StringBuilder sb = new StringBuilder();
                            sb.append(player.getRoom().getId());
                            for (Item item : player.getInventory()) {
                                sb.append("\n");
                                sb.append(item.getId());
                            }
                            saves.put(target, sb.toString());

                            adventureServer.sendMessage(player.getConnectionID(), "Location and items saved to \"" + target + "\"");
                            break;
                            //handles restoring
                        } else if (input.length() > 7 && input.substring(0, 8).equals("restore ")) {

                            Room targetRoom;
                            ArrayList<Item> itemsHeld = new ArrayList<>();

                            try (Scanner fileInput = new Scanner(saves.get(input.substring(8)))) {

                                targetRoom = Registration.<Room>getOwnerByStr("room_id", fileInput.next());
                                while (fileInput.hasNext()) {
                                    itemsHeld.add(Registration.<Item>getOwnerByStr("item_id", fileInput.next()));
                                }

                            } catch (NullPointerException ex) {
                                adventureServer.sendMessage(player.getConnectionID(), "Sorry, we couldn't find a restore file with that name.");
                                break;
                            } catch (NoSuchElementException ex) {
                                adventureServer.sendMessage(player.getConnectionID(), "Sorry, something went wrong with the file you specified.");
                                break;
                            }

                            parser.runPlayerInput(player, "drop all");
                            player.setRoom(targetRoom);
                            java.util.Set<InventoryMixin> allTheInventories = Registration.<InventoryMixin>getAllOfType(InventoryMixin.class);
                            for (Item item : itemsHeld) {
                                for (InventoryMixin im : allTheInventories) {
                                    if (im.itemPresent(item)) {
                                        im.remove(item);
                                    }
                                }
                                player.getInventoryMixin().add(item);
                            }
                            adventureServer.sendMessage(player.getConnectionID(), "Game restored.");
                            break;

                            //handles all other commands
                        } else if (player != null) {
                            adventureServer.sendMessage(e.getConnectionID(), parser.runPlayerInput(player, input));
                            //handles if the user has not created/taken over a charactor
                        } else {
                            adventureServer.sendMessage(e.getConnectionID(), "You have not created a charector or taken over" +
                                    " one");
                        }
                        //handles if the game has not been loaded
                    } else {
                        adventureServer.sendMessage(e.getConnectionID(), "The server has not been started with" +
                                " \"new-game\"");
                    }
                    break;
                case CONNECTION_TERMINATED:
                    System.out.println("Player Discconected: " + e.getConnectionID());
                    break;
                default:
                    break;
            }
        } catch (
                UnknownConnectionException unknownConnectionException) {
            System.out.println("ERROR: UnkownConnection, no big deal, does not affect server");
        }

    }

    /**
     * starts the server
     *
     * @param port port to start the server on
     */
    public void start(int port) {
        adventureServer.startServer(port);
    }

    /**
     * stops the server and handles all players
     *
     * @throws UnknownConnectionException if a connection can not be discconnected
     */
    public void stop() throws UnknownConnectionException {
        for (Player existPlayer : playerList) {
            if (existPlayer != null) {
                parser.runPlayerInput(existPlayer, "drop all");
            }
            if (!existPlayer.isHost()) {
                adventureServer.sendMessage(existPlayer.getConnectionID(), "CONNECTION TERMINATED: REASON HOST DISCONNECT");
            } else {
                adventureServer.sendMessage(existPlayer.getConnectionID(), "DISCONNECTED");
            }
            try {
                adventureServer.disconnect(existPlayer.getConnectionID());
            } catch (IOException error) {
                System.out.println("Error discconnecting players on server stop");
            }
        }
        adventureServer.stopServer();
        playerList = null;
        System.out.println("Server Stopped");
    }

    /**
     * loads all the info on a new game
     */
    public void initialize() {
        parser = new Parser();
        anotherLoader.loadStuff(parser);
    }

    /**
     * gets the Minotaur
     *
     * @return mino
     */
    public Minotaur getMino() {
        return mino;
    }
}
