package util;

import parser.Objection;

import java.util.List;
import java.util.ArrayList;

public class ObjectionComponent {

    private ArrayList<Objection> objections = new ArrayList<>();

    public List<Objection> getObjections() {
        return new ArrayList<>(objections);
    }

    public void addObjection(Objection obj) {
        objections.add(obj);
    }

    public boolean removeObjection(Objection obj) {
        return objections.remove(obj);
    }

    //public ObjectionComponent getObjectionComponent()
}
