package util;


public abstract class Mixin<O extends Composite,T> {

    private O owner;

    public Mixin(O owner, String className) {
        this.owner = owner;
    }

    public O getOwner() {
        return owner;
    }

    public abstract T get();

    protected String mixinId = null;
    public String getMixinId() {
        return mixinId;
    }

}
