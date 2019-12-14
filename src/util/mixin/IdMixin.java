package util.mixin;

import util.Mixin;
import util.Composite;
import util.Registration;

public class IdMixin<O extends Composite> extends Mixin<O, String> {
    //ONLY A COMPOSITE (using the appropriate mixin) SHOULD IMPLEMENT THIS (unless you want to override this)
    public interface Id {
        @SuppressWarnings("unchecked")
        public default String getId() {
            return ((Composite)this).<IdMixin>getTypeMixin("id").get();
        }
    }

    private String id;
    private Registration<IdMixin> compRegistrationId = null;

    @Override
    public String getMixinId() { return "id"; }

    public IdMixin(boolean register, O owner, String className, String id) {
        super(owner, className);
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
