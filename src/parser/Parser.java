package parser;

import util.Composite;
import util.Registration;
import util.mixin.ObjectionMixin;
import world.*;

import java.util.ArrayList;


/*
 *  Takes player input. Runs it. Returns the player output.
 *
 *  Date Last Modified: 12/05/19
 *	@author Sam VanderArk, Patrick Philbin, Thomas Grifka, Alex Hromada
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

public class Parser extends Composite implements ObjectionMixin.Objections {

    public static final Command UnrecognizedCommand;    // A fake command for when the player types nonsense
    static {    // I put this here because it's not really used outside of the Parser and might be changed with it
        UnrecognizedCommand = new Command(
            false,
            "unrecognized",
            new Response("Sorry, I don't recognize that command.|What's that?|Huh?|I'm not sure what you're trying to tell me.", 1, new Action[]{}){

                @Override
                public String getPlayerMessage(Player player) {
                    String[] answers = super.getPlayerMessage(player).split("\\|");
                    return answers[(int)(answers.length*Math.random())];
                }
            },
            new ArrayList<String>()
        );
    }

    private Minotaur minotaur;  //The minotaur. It's an Item, so we really shouldn't need this, actually.

    public Parser() {
        addMixin(new ObjectionMixin<>(this, "parser"));

    }

    /*
     * Runs player input. Changes the world state. Returns what they see.
     * @param player Who's doing import junit.framework.TestCase;
     * @param playerInput What they're trying to do
     * @return What the player sees in response.
     */
    public String runPlayerInput(Player player, String playerInput) {
        playerInput = playerInput.toLowerCase().trim();
        String[] inputStuff = playerInput.split("\\s+");
        playerInput = "";
        for(String bit : inputStuff) {
            playerInput += bit;
            playerInput += " ";
        }
        if(playerInput.length() > 0) {
            playerInput = playerInput.substring(0, playerInput.length() - 1);
        }

        Command command = null;
        String modifiedInput = playerInput;
        while(command == null || command.isReplace(playerInput)) {
            playerInput = modifiedInput;
            Command[] results = (Registration.<Command>searchOwnerByStr("command_name", playerInput)).toArray(new Command[]{});
            if(results.length == 0) {
                command = UnrecognizedCommand;
            } else {
                command = results[0];
                modifiedInput = command.replacementText(playerInput);
            }
        }
        playerInput = modifiedInput;

        Response response = command.getResponse(playerInput);
        response = anyObjections(player, command, response);

        String result = response.getPlayerMessage(player);
        response.run(player);   //since result might be affected by running it early
        return result;
    }

    /*
     * Checks for objections affecting the command; replaces the response if applicable
     * @param player The affected player
     * @param command The command that would be run
     * @param currentResponse The current response that would be the result of the command
     * @return A modified response, if needed; it gets returned unchanged if there are no objections
     */
    public Response anyObjections(Player player, Command command, Response currentResponse) {
        Response mostUrgentResponse = currentResponse;

        ArrayList<Objection> objectionsList = new ArrayList<>();
        objectionsList.addAll(this.getObjectionsList());
        objectionsList.addAll(player.getObjectionsList());
        objectionsList.addAll(player.getRoom().getObjectionsList());

        for(Objection obj : objectionsList) {
            Response tempResponse = obj.check(player, command, currentResponse);
            if(tempResponse != null && tempResponse.getSeverity() > mostUrgentResponse.getSeverity()) {
                mostUrgentResponse = tempResponse;
            }
        }

        return mostUrgentResponse;
    }

    /*
     * Gets the minotaur
     * @return the minotaur
     */
    public Minotaur getMinotaur() {
        return minotaur;
    }

    /*
     * Sets the minotaur
     * @param minotaur the minotaur
     */
    public void setMinotaur(Minotaur minotaur) {
        this.minotaur = minotaur;
    }

}
