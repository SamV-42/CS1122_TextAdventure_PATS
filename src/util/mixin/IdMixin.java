package util.mixin;

import util.Mixin;
import util.Composite;
import util.Registration;

public class IdMixin<O extends Composite> extends Mixin<O, String> {

    private String id;
    private Registration<IdMixin> compRegistrationId = null;

    public String getMixinId() { return "id"; }

    public IdMixin(boolean register, O owner, String className, String id) {
        super(owner);
        this.id = id;

        if(register) {
            String id_name = className + "_id";
            compRegistrationId = new Registration<>(this, id_name, id);
        }
    }

    public IdMixin(O owner, String className, String id) {
        this(true, owner, className, id);
    }

    @Override
    public String get() {
        return id;
    }
}
