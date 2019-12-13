package game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class JSONReader {


    private static List<Path> fileList = null;


    public static void main(String[] args) {

//        JSONReader reader = new JSONReader();
//        Path currentRelativePath = Paths.get("");
//        String s = currentRelativePath.toAbsolutePath().toString();
//        System.out.println("Current relative path is: " + s);
        streamDirectory();
    }

//    public JSONReader(){
//
//    }


    private static void streamDirectory() {

        try {
            Path currentRelativePath = Paths.get("");
            String data = currentRelativePath.toAbsolutePath().toString() + "/data";
//            Path dataPath = Paths.get("E:\\JavaStuff\\1122\\CS1122_TextAdventure_PATS\\data");

            fileList = Files.walk(currentRelativePath).filter(s -> s.toString().endsWith(".json")).map(Path::getFileName).sorted().collect(Collectors.toList());
            for (Path name : fileList
                 ) {
                System.out.println(name);
            }

        } catch (IOException e) {

        }
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
