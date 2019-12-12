package util;

public abstract class StandardMixin<O  extends Composite, T> extends Mixin<O, T[]> {

    public StandardMixin(O owner, String className) {
        super(owner, className);
    }

    public abstract void add(T t);
    public abstract void remove(T t);
}
