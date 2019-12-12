package world;

import parser.Objection;
import util.mixin.ObjectionMixin;
import util.Registration;
import util.Composite;
import util.mixin.IdMixin;
import util.mixin.InventoryMixin;
import util.mixin.PrimaryNameMixin;
import world.Item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuilder;

public class Room extends Composite {

    private String id;
    private String description;
    private String title;
    private HashMap<Direction, Room> connections = new HashMap<>();

    public Room(String id) {
        addMixin(new IdMixin<>(this, "room", id));
        addMixin(new ObjectionMixin<>(this, "room"));
        addMixin(new InventoryMixin<>(this, "room"));
        addMixin(new PrimaryNameMixin<>(this, "room", ""));
    }

    public String look() {
        StringBuilder fullDescription = new StringBuilder();

        fullDescription.append("--- ");
        fullDescription.append(getTitle());
        fullDescription.append(" ---\n");

        fullDescription.append(getDescription());

        Item[] inv = this.<InventoryMixin>getTypeMixin("inventory").get();
        if(inv.length == 0) {

        } else {
            fullDescription.append("\n\nYou can see ");

            ListMakerHelper help = new ListMakerHelper(inv.length, " here.\n");
            for(int i = 0; i < inv.length; ++i ) {
                fullDescription.append(inv[i].getArticle());
                fullDescription.append(" ");
                fullDescription.append(inv[i].getMixin("primaryname").get());
                fullDescription.append(help.getNextSeparator());
            }
        }

        if( connections.size() == 0) {

        } else if( connections.size() == 1 ) {
            fullDescription.append("\n\nThere is an exit ");
            fullDescription.append(connections.keySet().toArray(new Direction[]{})[0].getMixin("primaryname").get());
            fullDescription.append(".\n");
        } else {
            fullDescription.append("\n\nThere are exits ");

            ListMakerHelper help = new ListMakerHelper(connections.size(), ".\n");
            for(Iterator<Direction> iterator = connections.keySet().iterator(); iterator.hasNext(); ) {
                fullDescription.append(iterator.next().getMixin("primaryname").get());
                fullDescription.append(help.getNextSeparator());
            }
        }

        return fullDescription.toString();
    }

    private class ListMakerHelper {
        private String finalSep;
        private int length;
        private int index = 0;

        ListMakerHelper(int length, String finalSep) {
            this.length = length;
            this.finalSep = finalSep;
        }
        ListMakerHelper(int length) {
            this(length, ".");
        }
        public String getNextSeparator() {
            ++index;
            if(index < length - 1) {
                return ", ";
            } else if(index == length - 1) {
                if(length == 2) {
                    return " and ";
                } else {
                    return ", and ";
                }
            } else if(index == length) {
                return ".";
            } else {
                throw new java.lang.RuntimeException("Room Look Too Much");
            }
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Room getConnection(Direction dir) {
        return connections.get(dir);
    }

    public Room[] getConnectionDirs() {
        return connections.keySet().toArray(new Room[]{});
    }

    public boolean addConnection(Direction dir, Room room) {
        return connections.put(dir, room) != null;
    }

    public boolean removeConnection(Direction dir) {
        return connections.remove(dir) != null;
    }

    //Convenience methods
    public String getTitle() {
        return this.<PrimaryNameMixin>getTypeMixin("primaryname").get();
    }

    public void setTitle(String title) {
        this.<PrimaryNameMixin>getTypeMixin("primaryname").set(title);
    }
}
