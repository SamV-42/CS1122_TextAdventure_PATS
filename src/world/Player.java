package world;

import util.Registration;
import util.Composite;
import util.mixin.*;


import java.util.List;
import java.util.ArrayList;

public class Player extends Composite
                    implements IdMixin.Id, ObjectionMixin.Objections, PrimaryNameMixin.PrimaryName, InventoryMixin.Inventory {

    private Room room;

    public Player(String id) {
        addMixin(new IdMixin<>(this, "player", id));
        addMixin(new ObjectionMixin<>(this, "player"));
        addMixin(new PrimaryNameMixin<>(this, "player", id.toUpperCase()));
        addMixin(new InventoryMixin<>(this, "player"));
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}
