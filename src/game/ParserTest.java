package parser.filedata;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ParserTest {


    static String fileName = "data/room_test.json";

    static String json = "";

    public static void main(String[] args) throws IOException{
        json = parseFile(fileName);

        JSONObject obj = new JSONObject(json);
        JSONObject room = obj.getJSONObject("room");
        String[] names = obj.getNames(room);
        String id = room.getString("id");
        String title = room.getString("title");


        for (String n :
            names ) {
            System.out.println(n);
            System.out.println("reeeeeeeeeeee");
        }


        System.out.println(id);
        System.out.println(title);
//
//        JSONArray arr = obj.getJSONArray("posts");
//        for (int i = 0; i < arr.length(); i++) {
//            String post_id = arr.getJSONObject(i).getString("post_id");
//            System.out.println(post_id);
//        }

        try (Stream<Path> paths = Files.walk(Paths.get("data"))) {
            paths.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }



        System.out.println(parseFile(fileName));
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


    private static String readFile(BufferedReader reader) throws IOException{
        StringBuilder fileContent = new StringBuilder();

        int val;
        while((val = reader.read()) != -1){
            fileContent.append((char) val);
        }

        return fileContent.toString();


    }
}
