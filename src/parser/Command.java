package parser;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/*
 *  Represents a command typed by the player. Bridges the gap between various names that could be typed, and the actual effect.
 *  If you need a getResponse that depends on more complicated effects, feel free to extend it -- I recommend anonymous classes
 *
 *  Date Last Modified: 12/05/19
 *	@author Sam VanderArk, Patrick Philbin, Thomas Grifka, Alex Hromada
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

public class Command {

    /* Static variables and methods */

    /*
     * Returns a registered Command object if one of its names (case insensitive) (either primary or alt) matches
     * @param name The command name. If multiple words (eg WAKE UP, LOOK UNDER), it'll first try 1 word, then 2-word, etc. for the input
     * @return null if no such Command is found, or the chosen command if found.
     */
    public static Command searchCommandByName(String input) {
        input = input.toLowerCase();
        String[] splitCommand = input.trim().split("\\s+");

        String name = splitCommand[0];
        for(int i = 0; i < splitCommand.length; name += " " + splitCommand[i++]) {
            Command reg = registeredCommandsByName.get(name);
            if(reg != null) {
                return reg;
            }
        }
        return null;
    }

    /*
     * Returns a registered Command object if its internal id matches
     * @param id The command id.
     * @return null if no such Command is found, or the chosen command if found.
     */
    public static Command getCommandById(String id) {
        return registeredCommandsById.get(id);
    }


    private static HashMap<String, Command> registeredCommandsByName = new HashMap<>();   //maps names -> object (multiple keys -> one value)
    private static HashMap<String, Command> registeredCommandsById = new HashMap<>();   //maps id -> object


    /* Instance variables and methods */

    private String id;
    private Response response;
    private ArrayList<String> names;

    public Command(String id, Response response, List<String> names, boolean register) {
        this.id = id;
        this.response = response;

        if(register) {
            Command oldval = registeredCommandsById.put(this.getId(), this);
            if(oldval != null) {
                System.err.println("ERROR: Two commands with same id exist; registering the more recent one.\tId: "
                    + this.getId() + "\tOld Value: " + oldval.toString() + "\tNew Value: " + this.toString());
            }
        }
        if(register) {
            //this.names = new ArrayList<String>();
            this.names = new ArrayList<String>(names);
            for(String name : names) {
                registeredCommandsByName.put(name, this);
            }
        } else {
            this.names = new ArrayList<String>(names);
        }
    }

    public Command(String id, Response response, List<String> names) {
        this(id, response, names, true);
    }

    public Command(String id, Response response, String... names) {
        this(id, response, Arrays.asList(names));
    }

    /*
     * I put this here because it'll frequently be overridden by subclasses
     * In general: Override this if the response depends on the player input specifically (eg extracts the noun object);
     *  on the other hand, just create a custom Response if it just depends on world status (player location, etc.)
     * @param playerInput The player input that led to this command -- subclasses can extract the noun object the command will affect
     * @return The response to this command
     */
    public Response getResponse(String playerInput) {
        return response;
    }

    public String[] getNames() {
        return names.toArray(new String[]{});
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getId() {
        return id;
    }

    // No setId, deliberately -- id shouldn't be changed postcreation

}
