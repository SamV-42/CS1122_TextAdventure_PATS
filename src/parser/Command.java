package parser;

import util.Composite;
import util.mixin.IdMixin;
import util.mixin.NamesMixin;
import util.Registration;

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

public class Command extends Composite implements IdMixin.Id, NamesMixin.Names {

    /* Instance variables and methods */

    private String id;
    private Response response;
    private ArrayList<String> names;

    public Command(boolean register, String id, Response response, List<String> names) {
        addMixin(new IdMixin<>(this, "command", id));
        addMixin(new NamesMixin<>(this, "command", names));

        this.response = response;
    }

    /* Slightly-modified constructors from NamedObject, with response param added */
    public Command(String id, Response response, List<String> names) {
        this(true, id, response, names);
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

    public String replacementText(String playerInput) {
        String nameUsed = (Registration.searchIdentifierByStr("command_name", playerInput));
        return playerInput.equals(nameUsed) ? "" : playerInput.substring( nameUsed.length() + 1 );
    }

    //Does nothing much unless overridden by a replacing command
    public boolean isReplace(String playerInput) {
        return false;
    }

}
