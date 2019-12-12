package util;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public abstract class StandardMixin<O  extends Composite, T> extends Mixin<O, T[]> {

    protected interface StandardInterface {

        @SuppressWarnings("unchecked")
        protected static <U extends StandardMixin, Z extends StandardInterface> U getStandardMixin(Z thisThing, String mixinId) {
            return ((Composite)this).<U>getTypeMixin(mixinId);
        }

        protected static <U extends StandardMixin, V, Z extends StandardInterface> V[] getStandard(Z thisThing, String mixinId) {
            return this.<U,Z>getStandardMixin(mixinId).get();
        }

        protected static <U extends StandardMixin, V, Z extends StandardInterface> List<V> getStandardList(Z thisThing, String mixinId) {
            return new ArrayList<>(Arrays.asList(this.<U,V,Z>getStandard(mixinId)));
        }

    }

    /*protected static <S extends StandardMixin, E extends Composite> S interfaceGetMixin(E thisThing) {
        return thisThing.<S>getTypeMixin(thisThing.getMixinId());
    }

    protected static <T extends StandardMixin, V> V interfaceGet(String mixinId) {
        return <T>interfaceGetMixin(mixinId).get();
    }

    protected static <T extends StandardMixin, V> List<V> getInventoryList() {
        return new ArrayList<>(Arrays.asList( <T, V>interfaceGet(mixinId) ));
    }*/

    public StandardMixin(O owner, String className) {
        super(owner, className);
    }

    public abstract void add(T t);
    public abstract void remove(T t);
}
