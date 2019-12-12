package util;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public abstract class NamedObject extends IdObject {

    private RegistrationComponent<NamedObject> compRegistrationNames = null;
    private ArrayList<String> names;

    public NamedObject(String className, String id, List<String> names, boolean register) {
        super(className, id, register);

        names = new ArrayList<>(names);

        if(register) {
            String id_name = className + "_name";
            compRegistrationNames = new RegistrationComponent<>(this, id_name);
            for(String name : names) {
                compRegistrationNames.addIdentifier(name);
            }
        }
    }
    /*
    Unfortunately, constructors can't be inherited in Java. So,
    here's a "quick-start" to making the constructors for a Thing,
    a generic NamedObject (replace 'Thing' with your subclass name
    and "thing_id" with the relevant id, etc.

    import java.util.Arrays;
    import java.util.List;

    public Thing(String id, List<String> names, boolean register) {
        super("thing_id", id, names, register);
    }
    public Thing(String id, List<String> names) { this(id, names, true); }
    public Thing(String id, String... names) { this(id, Arrays.asList(names)); }
    public Thing(String id) {this(id, new String[]{}); }

    */

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

}
