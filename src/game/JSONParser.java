package game;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import world.Direction;
import world.Item;
import world.Room;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    List<Item> itemList = null;
    List<Room> roomList = null;
    private JSONReader jsonReader = new JSONReader();

    public void parseFile() {

        List<Path> files = jsonReader.streamDirectory();

        for (Path n :
                files) {

            if (n.toString().startsWith("item_")) {
                parseItem(n.toString());
            } else if (n.toString().startsWith("room_")) {
                parseRoom(n.toString());
            }


        }
    }


    private void parseItem(String fileName) {
        try {
            String json = jsonReader.parseFile(fileName);

            JSONObject obj = new JSONObject(json);
            JSONObject item = obj.getJSONObject("item`");
            String id = item.getString("id");
            String name = item.getString("name");
            String desc = item.getString("description");
            String attrib = item.getString("attrib");
            JSONArray altnames = item.getJSONArray("altnames");

            ArrayList<String> nameList = new ArrayList<>();
            for (int i = 0; i < altnames.length(); i++) {
                nameList.add(altnames.getJSONObject(i).getString("name_" + i));
            }

            Item newItem = new Item(id, name, nameList);
            newItem.setDescription(desc);

        } catch (IOException e) {

        }
    }

    private void parseRoom(String fileName) {

        try {
            String json = jsonReader.parseFile(fileName);

            JSONObject obj = new JSONObject(json);
            JSONObject room = obj.getJSONObject("item`");
            String id = room.getString("id");
            String title = room.getString("name");
            String desc = room.getString("description");
            JSONArray connection = room.getJSONArray("connection");
            JSONArray items = room.getJSONArray("items");

            ArrayList<String> connections = new ArrayList<>();

            Room newRoom = new Room(id);
            newRoom.setDescription(desc);

            try {
                for (Object n :
                        connection) {
                    switch (n.toString()) {
                        case "north":
//                            newRoom.addConnection(Direction.NORTH, )
                            break;
                        case "east":
                            break;
                        case "south":
                            break;
                        case "west":
                            break;

                    }

                }
            } catch (JSONException e) {

            }
            for (Object n :
                    connections) {
//                newRoom.addConnection(Direc);
            }

        } catch (IOException e) {

        }

    }

    private void direction(String dir) {


    }

}
