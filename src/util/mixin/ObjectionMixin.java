package util.mixin;

import parser.Objection;
import util.Composite;
import util.StandardMixin;
import util.Registration;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class ObjectionMixin<O extends Composite> extends StandardMixin<O, Objection>{

    public interface Objections {

        @SuppressWarnings("unchecked")
        public default ObjectionMixin getObjectionMixin() {
            return ((Composite)this).<ObjectionMixin>getTypeMixin("objection");
        }

        public default Objection[] getObjections() {
            return getObjectionMixin().get();
        }

        public default List<Objection> getObjectionsList() {
            return new ArrayList<>(Arrays.asList(getObjections()));
        }
    }


    private ArrayList<Objection> objections = new ArrayList<>();

    @Override
    public String getMixinId() { return "objection"; }

    public ObjectionMixin(O owner, String className, List<Objection> objections) {
        super(owner, className);
        this.objections = new ArrayList<>(objections);
    }

    public ObjectionMixin(O owner, String className, Objection... objections) {
        this(owner, className, Arrays.asList(objections));
    }

    @Override
    public Objection[] get() {
        return objections.toArray(new Objection[]{});
    }

    @Override
    public void add(Objection t) {
        objections.add(t);
    }

    @Override
    public void remove(Objection t) {
        objections.remove(t);
    }
}
