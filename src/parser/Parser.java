package parser;

import game.DataLoader;

/*
 *  Takes player input. Runs it. Returns the player output.
 *
 *  Date Last Modified: 12/05/19
 *	@author Sam VanderArk, Patrick Philbin, Thomas Grifka, Alex Hromada
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

public class Parser {

    public static final Command UnrecognizedCommand;    // A fake command for when the player types nonsense
    static {    // I put this here because it's not really used outside of the Parser and might be changed with it
        UnrecognizedCommand = new Command(
            "unrecognized", 
            new Response("Sorry, I don't recognize that command.|What's that?|Huh?|I'm not sure what you're trying to tell me.", 1, new Action[0]{}){

                @Override
                public String getPlayerMessage(Player player) {
                    String[] answers = super.getPlayerMessage().split("|");
                    return answers[math.randInt(0, answers.length)];
                }
            }, 
            new String[0]{},
            false
        );
    }

    private ArrayList<Objection> globalObjections = new ArrayList<Objection>(); //List of miscellaneous objections not tied to a specific room or player

    public Parser() {
        DataLoader dataLoader = new DataLoader();
        dataLoader.generateCommands();
    }

    public String runPlayerInput(Player player, String playerInput) {
        Command command = Command.searchCommandByName(playerInput);
        if(command == null) {
            command = UnrecognizedCommand;
        }

        Response response = command.getResponse(playerInput);
        response = anyObjections(player, command, response);

        response.run(player);
        return response.getPlayerMessage(player);
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

        objectionsList = new ArrayList<Objection>();
        objectionsList.addAll(globalObjections);
        objectionsList.addAll(player.getObjections());
        objectionsList.addAll(player.getRoom().getObjections());

        for(Objection obj : objectionsList) {
            Response tempResponse = obj.check(player, command);
            if(tempResponse != null && tempResponse.getSeverity() > mostUrgentResponse.getSeverity()) {
                mostUrgentResponse = tempResponse;
            }
        }

        return mostUrgentResponse;
    }

    public void addGlobalObjection(Objection obj) {
        globalObjections.add(obj);
    }

    public void removeGlobalObjection(Objection obj) {
        globalObjections.remove(obj);
    }

}
