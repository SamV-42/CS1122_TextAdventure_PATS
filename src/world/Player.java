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
    private boolean host;
    long connectionId;

    public Player(String id, long connectionId) {
        addMixin(new IdMixin<>(this, "player", id));
        addMixin(new ObjectionMixin<>(this, "player"));
        addMixin(new PrimaryNameMixin<>(this, "player", id.toUpperCase()));
        this.connectionId = connectionId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setHost(boolean host){
        this.host = host;
    }

    public boolean isHost() {
        return host;
    }

    public long getConnectionID () {
        return connectionId;
    }
}
