package parser;

import java.util.ArrayList;

/*
 *  Represents a command typed by the player. Bridges the gap between various names that could be typed, and the actual effect.
 *  If you need a getResponse that depends on more complicated effects, feel free to extend it -- I recommend anonymous classes
 *
 *  Date Last Modified: 12/05/19
 *	@author Sam VanderArk, Patrick Philbin, Thomas Grifka, Alex Hromada
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

public abstract class Command {

    /* Static variables and methods */

    /*
     * Returns a registered Command object if one of its names (either primary or alt) matches 
     * @param name The command name. Currently haven't decided whether to allow multi-word commands (eg WAKE UP, TALK TO, etc.)
     * @return null if no such Command is found, or the chosen command if found.
     */
    public static Command getCommandByName(String name) { /* ... */ }

    /*
     * Returns a registered Command object if its internal id matches 
     * @param id The command id.
     * @return null if no such Command is found, or the chosen command if found.
     */
    public static Command getCommandById(String id) { /* ... */ }


    private static HashMap<String, Command> registeredCommandsByName;   //maps names -> object (multiple keys -> one value)
    private static HashMap<String, Command> registeredCommandsById;   //maps id -> object


    /* Instance variables and methods */

    private String id;
    private Response response;
    private ArrayList<String> names;

    public Command(String id, Response response, ArrayList<String> names, boolean register) {
        this.id = id;
        if(register) {
            Command oldval = registeredCommandsById.put(command.getId(), command);
            if(oldval != null) {
                System.err.println("ERROR: Two commands with same id exist; registering the more recent one.\Id: " 
                    + command.getId() + "\tOld Value: " + oldval.toString() + "\tNew Value: " + command.toString());
            }
        }

        this.response = response;

        if(register) {
            names = new ArrayList<String>();
            for(String name : names) {
                addName(name);
            }
        } else {
            this.names = names;
        }
    }

    public Command(String id, Response response, ArrayList<String> names) {
        super(id, response, names, true);
    }

    public Command(String id, Response response, String... names) {
        this(id, response, new ArrayList<String>(names.asList()));
    }

    public Command(String id, Response response, String[] names) {
        this(id, response, new ArrayList<String>(names.asList()));
    }

    public boolean addName(String name) {
        names.add(name);

        Command oldval = registeredCommandsByName.put(name, command);
        if(oldval != null) {
            System.err.println("WARNING: Reassigning a command name input.\tName: " + name 
                    + "\tOld Value: " + oldval.toString() + "\tNew Value: " + command.toString());
            return false;
        }
        return true;
    }

    public boolean removeName(String name) {
        names.remove(name);
        return (registeredCommandsByName.remove(name) != null);
    }

    public String[] getNames() {
        return names.toArray();
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getId() {
        return id;
    }

    // No setId, deliberately -- id shouldn't be changed postcreation

}
