package world;

import util.Composite;
import util.mixin.*;

public class Player extends Composite
        implements IdMixin.Id, ObjectionMixin.Objections, PrimaryNameMixin.PrimaryName, InventoryMixin.Inventory {

    private Room room;
    private boolean host;
    long connectionId;

    public Player(String id, long connectionId) {
        addMixin(new IdMixin<>(this, "player", id));
        addMixin(new ObjectionMixin<>(this, "player"));
        addMixin(new PrimaryNameMixin<>(this, "player", id.toUpperCase()));
        addMixin(new InventoryMixin<>(this, "player"));
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

    public void setConnectionID(long id){ connectionId = id; }

    public void kill(){
        parser.runPlayerInput(this, "drop all");
        this.setRoom(Registration.getOwnerByStr("room_id", "entrance"));
    }

}