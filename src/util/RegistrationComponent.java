package util;

import parser.Objection;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class RegistrationComponent<T> {

    //maps table name to (maps name/id/etc to the obj)
    private static HashMap<String, HashMap<String, ?> > registered = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getByStr(String identifierName, String identifier) {
        return (T)(getMapOrThrow(identifierName).get(identifier));
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

    private RegistrationComponent() {};

    // It is *strongly* recommended to use an identifier name with the classname appended prior, eg room_id
    public RegistrationComponent(T owner, String identifierName) {
        this.owner = owner;
        this.identifierName = identifierName;
        if(registered.get(identifierName) == null) {
            registered.put(identifierName, new HashMap<String, T>());
        }
    }

    public RegistrationComponent(T owner, String identifierName, String identifier) {
        this(owner, identifierName);
        addIdentifier(identifier);
    }

    @SuppressWarnings("unchecked")
    public boolean addIdentifier(String identifier) {
        T oldval = RegistrationComponent.<T>getMapOrThrow(identifierName).put(identifier, owner);
        if(oldval != null) {
            System.err.println("WARNING: Reassigning registration identifier. {type: " + oldval.getClass().getName()
                + ", identifier type: " + identifierName + ", idenitifer value: " + identifier
                + ", old object tostring: " + oldval.toString() + ", new object tostring: " + owner.toString() + " }");
            return false;
        }
        return true;
    }

    public boolean removeIdentifier(String identifier) {
        return (RegistrationComponent.<T>getMapOrThrow(identifierName).remove(identifier) != null);
    }

}
