package util.mixin;

import util.Mixin;
import util.Composite;
import util.Registration;

public class PrimaryNameMixin<O extends Composite> extends Mixin<O, String> {

    public interface PrimaryName {

        @SuppressWarnings("unchecked")
        public default PrimaryNameMixin getPrimaryNameMixin() {
            return ((Composite)this).<PrimaryNameMixin>getTypeMixin("primaryname");
        }

        public default String getPrimaryName() {
            return getPrimaryNameMixin().get();
        }

        public default void setPrimaryName(String name) {
            getPrimaryNameMixin().set(name);
        }
    }

    private String name;

    @Override
    public String getMixinId() { return "primaryname"; }

    public PrimaryNameMixin(O owner, String className, String name) {
        super(owner, className);
        this.name = name;
    }

    @Override
    public String get() {
        return name;
    }

    public void set(String name) {
        this.name = name;
    }
}
