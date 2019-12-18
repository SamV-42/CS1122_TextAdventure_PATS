package util;

import java.util.*;
import util.mixin.IdMixin;

/**
*	This class handles static registration across the program.
*   Note this essentially implies that, barring a prefix on the identifier name, there's only one server instance on the same program.
*   A Registration object stores a reference in the global static HashMap, letting you relate identifiers to objects under a particular general identifier_name
*
*   Date Last Modified: 12/18/19
*	@author Thomas Grifka, Sam VanderArk, Patrick Philbin, Alex Hromada
*
*	CS112, Fall 2019
*	Lab Section 2
*/


public class Registration<T> {

    private static HashMap<String, HashMap<String, ?> > registered = new HashMap<>();   //maps table name to (maps name/id/etc to the obj)

    /*
     * Gets a particular mixin based on its identifier name and identifier
     * @param identifierName for instance, "item_id"
     * @param identifier for instance, "golden_axe"
     * @return The mixin, or null if no such thing has been registered
     */
    @SuppressWarnings("unchecked")
    public static <T> T getByStr(String identifierName, String identifier) {
        return (T)(getMapOrThrow(identifierName).get(identifier));
    }

    /*
     * Gets a particular mixin's owner based on its identifier name and identifier
     * @param identifierName for instance, "item_id"
     * @param identifier for instance, "golden_axe"
     * @return The mixin's owner, or null if no such thing has been registered
     */
    @SuppressWarnings("unchecked")
    public static <T extends Composite> T getOwnerByStr(String identifierName, String identifier) {
        Mixin mix = getByStr(identifierName, identifier);
        return (mix == null) ? null : (T)(mix.getOwner());
    }

    /*
     * Gets a list of mixins fuzzily-matching the search query
     * @param identifierName for instance, "item_id"
     * @param input for instance, "torch" or whatever the player entered
     * @return A list of objects matching the search
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> searchByStr(String identifierName, String input) {
        String[] identifiers = searchIdentifierByStr(identifierName, input).toArray(new String[]{});
        ArrayList<T> listr = new ArrayList<>();
        for(String identifier : identifiers) {
            listr.add(getByStr(identifierName, identifier));
        }
        return listr;
    }

    /*
     * Returns a list of matching identifiers -- if one of the names (case insensitive) matches
     * @param input The selected bit of input. If multiple words (eg WAKE UP, LOOK UNDER), it'll first try 1 word, then 2-word, etc. for the input
     * @return A list of matching identifiers
     */
    public static List<String> searchIdentifierByStr(String identifierName, String input) {
        input = input.toLowerCase();
        String[] splitCommand = input.trim().split("\\s+");

        ArrayList<String> names = new ArrayList<>();

        String name = "";
        for(int i = 0; i < splitCommand.length; ) {
            name += splitCommand[i++];
            Object reg = getByStr(identifierName, name);
            if(reg != null) {
                names.add(name);
            }
            name += " ";
        }
        return names;
    }

    /*
     * Gets a list of mixins' owners fuzzily-matching the search query
     * @param identifierName for instance, "item_id"
     * @param input for instance, "torch" or whatever the player entered
     * @return A list of owners matching the search
     */
    @SuppressWarnings("unchecked")
    public static <T extends Composite> List<T> searchOwnerByStr(String identifierName, String input) {
        List<Mixin> mix = Registration.<Mixin>searchByStr(identifierName, input);
        ArrayList<T> owners = new ArrayList<>();
        for (Mixin m : mix) {
            owners.add((T)(m.getOwner()));
        }
        return owners;
    }

    /*
     * Gets a particular map based on identifier names
     * @param identifierName for instance, "item_id"
     * @throws NoSuchElementException if no such map exists
     * @return the given map
     */
    @SuppressWarnings("unchecked")
    private static <T> HashMap<String, T> getMapOrThrow(String identifierName) {
        HashMap<String, T> map = (HashMap<String, T>)registered.get(identifierName);
        if(map == null) {
            System.err.println("ERROR: Identifier was not registered (\"" + identifierName + "\")");
            throw new NoSuchElementException("Bad identifier");
        }
        return map;
    }

    /*
     * Gets all mixins of a given type
     * @param The class to be searched -- eg, IdMixin.class
     * @return A Set<> of the mixins
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> getAllOfType(Class<T> cls) {
        Set<T> output = new java.util.HashSet<>();
        for(HashMap<String, ?> map : registered.values() ) {
            for(Object t : map.values() ) {
                if(cls.isInstance(t)) {
                    output.add((T)t);
                }
            }
        }
        return output;
    }

    /*
     * Gets all owners of a given type that implement IdMixin.Id
     * @param The class to be searched -- eg, Item.class or Player.class
     * @return A Set<> of the objects
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> getAllOwnersOfType(Class<T> cls) {
        Set<IdMixin> ids = getAllOfType(IdMixin.class);
        Set<T> owners = new HashSet<>();
        for(IdMixin id : ids) {
            if(cls.isInstance(id.getOwner())) {
                owners.add((T)(id.getOwner()));
            }
        }
        return owners;
    }


    private String identifierName;      //the identifier name this Registration handles
    private T owner;                    //the owner of this Registration (probably a mixin)

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

    /*
     * Add a particular identifier
     * @param the identifier to be added
     * @return whether or not it overwrote a previous identifier (try to avoid this)
     */
    @SuppressWarnings("unchecked")
    public boolean addIdentifier(String identifier) {
        T oldval = Registration.<T>getMapOrThrow(identifierName).put(identifier, owner);
        if(oldval != null) {
            System.err.println("WARNING: Reassigning registration identifier. {type: " + oldval.getClass().getName()
                + ", identifier type: " + identifierName + ", identifier value: " + identifier
                + ", old object tostring: " + oldval.toString() + ", new object tostring: " + owner.toString() + " }");
            return false;
        }
        return true;
    }

    /*
     * Remove a particular identifier
     * @param the identifier to be removed
     * @return whether or not it actually removed anything
     */
    public boolean removeIdentifier(String identifier) {
        return (Registration.<T>getMapOrThrow(identifierName).remove(identifier) != null);
    }

}
