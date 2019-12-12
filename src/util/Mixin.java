package util;


public abstract class Mixin<O extends Composite,T> {

    private O owner;

    public Mixin(O owner) {
        this.owner = owner;
    }

    public O getOwner() {
        return owner;
    }

    public abstract T get();

    public abstract String getMixinId();

}
