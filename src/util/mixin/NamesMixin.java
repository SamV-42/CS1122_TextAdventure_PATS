package util.mixin;

import util.Composite;
import util.Registration;
import util.StandardMixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamesMixin<O extends Composite> extends StandardMixin<O, String> {

    public interface Names {

        @SuppressWarnings("unchecked")
        public default NamesMixin getNamesMixin() {
            return ((Composite)this).<NamesMixin>getTypeMixin("name");
        }

        public default String[] getNames() {
            return getNamesMixin().get();
        }

        public default List<String> getNamesList() {
            return new ArrayList<>(Arrays.asList(getNames()));
        }
    }

    private Registration<NamesMixin> compRegistrationNames = null;
    private ArrayList<String> names;

    @Override
    public String getMixinId() { return "name"; }

    public NamesMixin(boolean register, O owner, String className, List<String> names) {
        super(owner, className);

        this.names = new ArrayList<>(names);

        if(register) {
            String id_name = className + "_name";
            compRegistrationNames = new Registration<>(this, id_name);
            for(String name : names) {
                compRegistrationNames.addIdentifier(name);
            }
        }
    }

    public NamesMixin(O owner, String className, List<String> names) {
        this(true, owner, className, names);
    }

    public NamesMixin(O owner, String className, String... names) {
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
