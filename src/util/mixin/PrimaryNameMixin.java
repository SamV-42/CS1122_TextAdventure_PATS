package util.mixin;

import util.Mixin;
import util.Composite;
import util.Registration;

public class PrimaryNameMixin<O extends Composite> extends Mixin<O, String> {

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
