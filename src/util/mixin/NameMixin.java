package util.mixin;

import util.Composite;
import util.StandardMixin;
import util.Registration;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class NameMixin<O extends Composite> extends StandardMixin<O, String> {

    private Registration<NameMixin> compRegistrationNames = null;
    private ArrayList<String> names;

    public String getMixinId() { return "name"; }

    public NameMixin(boolean register, O owner, String className, List<String> names) {
        super(owner);

        this.names = new ArrayList<>(names);

        if(register) {
            String id_name = className + "_name";
            compRegistrationNames = new Registration<>(this, id_name);
            for(String name : names) {
                compRegistrationNames.addIdentifier(name);
            }
        }
    }

    public NameMixin(O owner, String className, List<String> names) {
        this(true, owner, className, names);
    }

    public NameMixin(O owner, String className, String... names) {
        this(owner, className, Arrays.asList(names));
    }

    @Override
    public String[] get() {
        return names.toArray(new String[]{});
    }

    @Override
    public void add(String t) {
        names.add(t);
        compRegistrationNames.addIdentifier(t);
    }

    @Override
    public void remove(String t) {
        names.remove(t);
        compRegistrationNames.removeIdentifier(t);
    }
}
