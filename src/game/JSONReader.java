package game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class JSONReader {


    public JSONReader() {

    }

    public List<Path> streamDirectory() {
        List<Path> fileList = null;
        try {
            Path currentRelativePath = Paths.get("");
            String data = currentRelativePath.toAbsolutePath().toString() + "/data";

            fileList = Files.walk(currentRelativePath).filter(s -> s.toString().endsWith(".json")).map(Path::getFileName).sorted().collect(Collectors.toList());
            for (Path name : fileList
                 ) {
                System.out.println(name);
            }

        } catch (IOException e) {

        }

        return fileList;
    }


    public String parseFile(String fileName) throws IOException {

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
