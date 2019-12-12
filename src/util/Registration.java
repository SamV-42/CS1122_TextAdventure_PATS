package util;

import parser.Objection;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Registration<T> {

    //maps table name to (maps name/id/etc to the obj)
    private static HashMap<String, HashMap<String, ?> > registered = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getByStr(String identifierName, String identifier) {
        return (T)(getMapOrThrow(identifierName).get(identifier));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Composite> T getOwnerByStr(String identifierName, String identifier) {
        Mixin mix = getByStr(identifierName, identifier);
        return (mix == null) ? null : (T)(mix.getOwner());
    }

    /*
     * Returns a registered object if one of its names (case insensitive) (either primary or alt) matches
     * @param input The selected bit of input. If multiple words (eg WAKE UP, LOOK UNDER), it'll first try 1 word, then 2-word, etc. for the input
     * @return null if no such Command is found, or the chosen command if found.
     */
    public static <T> T searchByStr(String identifierName, String input) {
        input = input.toLowerCase();
        String[] splitCommand = input.trim().split("\\s+");

        String name = splitCommand[0];
        for(int i = 0; i < splitCommand.length; name += " " + splitCommand[i++]) {
            T reg = getByStr(identifierName, name);
            if(reg != null) {
                return reg;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Composite> T searchOwnerByStr(String identifierName, String input) {
        Mixin mix = searchByStr(identifierName, input);
        return (mix == null) ? null : (T)(mix.getOwner());
    }


    @SuppressWarnings("unchecked")
    private static <T> HashMap<String, T> getMapOrThrow(String identifierName) {
        HashMap<String, T> map = (HashMap<String, T>)registered.get(identifierName);
        if(map == null) {
            System.err.println("ERROR: Identifier was not registered (\"" + identifierName + "\")");
            throw new NoSuchElementException("Bad identifier");
        }
        return map;
    }

    private String identifierName;
    private T owner;

    private Registration() {};

    // It is *strongly* recommended to use an identifier name with the classname appended prior, eg room_id
    public Registration(T owner, String identifierName) {
        this.owner = owner;
        this.identifierName = identifierName;
        if(registered.get(identifierName) == null) {
            registered.put(identifierName, new HashMap<String, T>());
        }
    }

    public Registration(T owner, String identifierName, String identifier) {
        this(owner, identifierName);
        addIdentifier(identifier);
    }

    @SuppressWarnings("unchecked")
    public boolean addIdentifier(String identifier) {
        T oldval = Registration.<T>getMapOrThrow(identifierName).put(identifier, owner);
        if(oldval != null) {
            System.err.println("WARNING: Reassigning registration identifier. {type: " + oldval.getClass().getName()
                + ", identifier type: " + identifierName + ", idenitifer value: " + identifier
                + ", old object tostring: " + oldval.toString() + ", new object tostring: " + owner.toString() + " }");
            return false;
        }
        return true;
    }

    public boolean removeIdentifier(String identifier) {
        return (Registration.<T>getMapOrThrow(identifierName).remove(identifier) != null);
    }

}
