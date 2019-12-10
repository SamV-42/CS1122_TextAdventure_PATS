package world;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuilder;

import parser.Objection;

public class Room {

    private String id;
    private String description;
    private String title;
    private HashMap<Direction, Room> connections = new HashMap<>();
    private ObjectionComponent objections = new ObjectionComponent();

    public String look() {
        StringBuilder fullDescription = new StringBuilder();

        fullDescription.append("--- ");
        fullDescription.append(getTitle());
        fullDescription.append(" ---\n");

        fullDescription.append(getDescription());

        Direction[] connectionsList = connections.keySet().toArray(new Direction[]{});

        if( connectionsList.length == 0 ) {

        } else if( connectionsList.length == 1 ) {
            fullDescription.append("\n\nThere is an exit ");
            fullDescription.append(connectionsList[0].getName());
            fullDescription.append(".");
        } else {
            fullDescription.append("\n\nThere are exits ");

            fullDescription.append(connectionsList[0].getName());

            for(int i = 1; i < connectionsList.length - 1; ++i ) {
                fullDescription.append(", ");
                fullDescription.append(connectionsList[i].getName());
            }
            if( connections.size() == 2 ) {
                fullDescription.append(" and ");
            } else {
                fullDescription.append(", and ");
            }
            fullDescription.append(connectionsList[connectionsList.length - 1].getName());
            fullDescription.append(".");
        }

        /* Here, add a description of the items present in the room */

        fullDescription.append("\n");
        return fullDescription.toString();
    }

    public Room(String id) {
        this.id = id;
    }

    public ObjectionComponent getObjectionComponent() {
        return objections;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Room getConnection(Direction dir) {
        return connections.get(dir);
    }

    public Direction[] getConnectionDirs() {
        return connections.keySet().toArray(new Direction[]{});
    }

    public boolean addConnection(Direction dir, Room room) {
        return connections.put(dir, room) != null;
    }

    public boolean removeConnection(Direction dir) {
        return connections.remove(dir) != null;
    }

    public String getId() {
        return id;
    }
}
