package game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class JSONReaderTest {


    static String fileName = "data/items/item_test_1.json";

    static String json = "";

    public static void main(String[] args) throws IOException {
        json = parseFile(fileName);

        JSONObject obj = new JSONObject(json);
        JSONObject item = obj.getJSONObject("item");
//        String[] names = obj.getNames(item);
        String id = item.getString("id");
        String name = item.getString("name");
        String description = item.getString("description");
        JSONArray altnames = item.getJSONArray("altnames");



//        for (String n :
//                names) {
//            System.out.println(n);
//            System.out.println("reeeeeeeeeeee");
//        }

        String roomNorth = "";
        String roomEast = "";
        String roomWest = "";

        System.out.println(id);
        System.out.println(name);

        try {
//            for (int i = 0; i < connection.length(); i++
//            ) {
//
//                roomNorth = connection.getJSONObject(i).getString("north");
//                roomEast = connection.getJSONObject(i).getString("east");
//                roomWest = connection.getJSONObject(i).getString("west");
//
//            }

//            for (Object n:
//                 altnames) {
//                int i = 0;
//
//                System.out.println(altnames.getJSONObject().getString("name_" + (i)));
//            }

            ArrayList<String> list = new ArrayList<>();
            for (Object n: altnames
                 ) {
                System.out.println(n.toString());
                list.add(n.toString());
            }

            System.out.println("=========================");
            for (String n :
                list  ) {
                System.out.println(n);
            }


            int j = altnames.length();
            for (int i = 0; i < altnames.length(); i++) {
                System.out.println(altnames.getJSONObject(i).getString("name"));
                j++;
            }
        } catch (JSONException e) {

        }


//        System.out.println(roomNorth);



//        JSONArray arr = obj.getJSONArray("posts");
//        for (int i = 0; i < arr.length(); i++) {
//            String post_id = arr.getJSONObject(i).getString("post_id");
//            System.out.println(post_id);
//        }

        try (Stream<Path> paths = Files.walk(Paths.get("data", "items"))) {
            paths.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(parseFile(fileName));
    }


    private static void readRooms() {

    }



    private static String parseFile(String fileName) throws IOException {

        String json2 = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            json2 = readFile(reader);
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }

        return json2;
    }


    private static String readFile(BufferedReader reader) throws IOException {
        StringBuilder fileContent = new StringBuilder();

        int val;
        while ((val = reader.read()) != -1) {
            fileContent.append((char) val);
        }

        return fileContent.toString();


    }
}
