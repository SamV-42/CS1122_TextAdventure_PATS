package world;

import parser.Objection;

import java.util.List;
import java.util.ArrayList;

public class Player {

    private Room room;
    private ObjectionComponent objections = new ObjectionComponent();

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public ObjectionComponent getObjectionComponent() {
        return objections;
    }
    
}
