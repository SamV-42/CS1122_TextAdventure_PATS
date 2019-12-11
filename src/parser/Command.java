package parser;

import util.RegistrationComponent;

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
            Command reg = RegistrationComponent.getByStr("command_name", name);
            if(reg != null) {
                return reg;
            }
        }
        return null;
    }

    /* Instance variables and methods */

    private String id;
    private Response response;
    private ArrayList<String> names;

    private RegistrationComponent<Command> compRegistrationId = null;
    private RegistrationComponent<Command> compRegistrationNames = null;

    public Command(String id, Response response, List<String> names, boolean register) {
        this.id = id;
        this.response = response;

        this.names = new ArrayList<String>(names);

        if(register) {
            compRegistrationId = new RegistrationComponent<>(this, "command_id", id);
            compRegistrationNames = new RegistrationComponent<>(this, "command_name");
            for(String name : names) {
                compRegistrationNames.addIdentifier(name);
            }
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

    public void setResponse(Response response) {
        this.response = response;
    }

    public String[] getNames() {
        return names.toArray(new String[]{});
    }

    public void addName(String name) {
        names.add(name);
        compRegistrationNames.addIdentifier(name);
    }

    public void removeName(String name) {
        names.remove(name);
        compRegistrationNames.removeIdentifier(name);
    }

    public String getId() {
        return id;
    }

    // No setId, deliberately -- id shouldn't be changed postcreation

}
