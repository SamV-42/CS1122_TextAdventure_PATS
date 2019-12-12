package parser;

import util.RegistrationComponent;
import util.NamedObject;

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

public class Command extends NamedObject {

    /* Instance variables and methods */

    private String id;
    private Response response;
    private ArrayList<String> names;

    public Command(String id, Response response, List<String> names, boolean register) {
        super("command", id, names, register);

        this.response = response;
    }

    /* Slightly-modified constructors from NamedObject, with response param added */
    public Command(String id, Response response, List<String> names) {
        this(id, response, names, true);
    }
    public Command(String id, Response response, String... names) {
        this(id, response, Arrays.asList(names));
    }
    public Command(String id, Response response) {
        this(id, response, new String[]{});
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

}
