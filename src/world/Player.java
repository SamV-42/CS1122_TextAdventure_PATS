package world;

import util.Registration;
import util.Composite;
import util.mixin.IdMixin;
import util.mixin.ObjectionMixin;
import util.mixin.PrimaryNameMixin;


import java.util.List;
import java.util.ArrayList;

public class Player extends Composite {

    private Room room;

    public Player(String id) {
        addMixin(new IdMixin<>(this, "player", id));
        addMixin(new ObjectionMixin<>(this, "player"));
        addMixin(new PrimaryNameMixin<>(this, "player", id.toUpperCase()));
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}
