package parser;

/*
 *  Takes player input. Runs it. Returns the player output.
 *
 *  Date Last Modified: 12/05/19
 *	@author Sam VanderArk, Patrick Philbin, Thomas Grifka, Alex Hromada
 *	CS1122, Fall 2019
 *	Lab Section 2
 */

public class Parser {

    public static final Command UnrecognizedCommand;    //static initialization block is below
    static {
        UnrecognizedCommand = new Command(
            "unrecognized", 
            new Response("Sorry, I don't recognize that command.|What's that?|Huh?|I'm not sure what you're trying to tell me.", 1, new Action[0]{}){

                @Override
                public String getPlayerMessage() {
                    String[] answers = super.getPlayerMessage().split("|");
                    return answers[math.randInt(0, answers.length)];
                }
            }, 
            new String[0]{},
            false
        );
    }

    private ArrayList<Objection> globalObjections = new ArrayList<Objection>();

    public Parser() {

    }

    public String runPlayerInput(Player player, String playerInput) {
        Command command = Command.getCommandByName(playerInput);
        if(command == null) {
            command = UnrecognizedCommand;
        }

        Response response = parseCommand(player, command);

        response.run(player);
        return response.getPlayerMessage();
    }

    public Response parseCommand(Player player, Command command) {
        Response mostUrgentResponse = command.getResponse();

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
