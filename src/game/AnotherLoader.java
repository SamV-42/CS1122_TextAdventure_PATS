package game;

import util.Registration;
import world.Item;
import world.Room;
import world.Minotaur;
import parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;



public class AnotherLoader {

    @SuppressWarnings("unchecked")
    public void loadStuff(Parser parser) {
        DataLoader dl = new DataLoader();
        dl.generateCommands();

        String[] inputFiles = {"../data/data"};
        for(String target : inputFiles) {
            this.subLoadStuff(target);
        }

        parser.setMinotaur(new Minotaur(Registration.<Room>getOwnerByStr("room_id", "min_den")));
        dl.putRoomBlockers(parser);
    }

    private void subLoadStuff(String target) {
        try(Scanner input = new Scanner(new File(target))) {
            Object currentObject = new Object();
            String currentId = "";
            String currentTypeName = "";
            Room dummyRoom = new Room("dummy_room");
            Item dummyItem = new Item("dummy_item", "dummydumbdumbs");

            int i = 0;
            while(input.hasNext()) {
                ++i;
                String line = input.nextLine();
                if(line.trim().equals("")) { continue; }

                String tag = line.split(">")[0];
                line = line.substring(tag.length() + 1);

                try {
                    switch(tag) {
                        case "type":
                            currentTypeName = line.trim();
                            break;
                        case "id":
                            line = line.trim();
                            switch(currentTypeName) {
                                case "room":
                                    currentObject = Registration.getOwnerByStr("room_id", line);
                                    if(currentObject == null) { currentObject = new Room(line); }
                                    break;
                                case "item":
                                    currentObject = Registration.getOwnerByStr("item_id", line);
                                    if(currentObject == null) { currentObject = new Item(line, "__temp"+i); }
                                    break;
                                default:
                                    System.err.println("Fatal Error: Wrong type tag used.l" + i);
                                    return;
                            }
                            break;
                        case "description":
                            if(currentObject instanceof Room) { ((Room)(currentObject)).setDescription(line); }
                            if(currentObject instanceof Item) { ((Item)(currentObject)).setDescription(line); }
                            break;
                        case "title":
                            ((Room)currentObject).setTitle(line);
                            break;
                        case "stuff":
                            Item item = Registration.getOwnerByStr("item_id", line);
                            if(item == null) { item = new Item(line, "__temp"+i); }
                            ((Room)currentObject).getInventoryMixin().add(item);
                            break;
                        case "connection":
                            Room otherRoom = Registration.getOwnerByStr("room_id", line.split(" ")[1]);
                            if(otherRoom == null) { otherRoom = new Room(line.split(" ")[1]); }
                            ((Room)currentObject).addConnection(Registration.getOwnerByStr("direction_id", line.split(" ")[0]), otherRoom);
                            break;
                        case "name":
                            ((Item)currentObject).setPrimaryName(line.toLowerCase().trim());
                            ((Item)currentObject).getNamesMixin().remove("");
                        case "altname":
                            ((Item)currentObject).getNamesMixin().add(line.toLowerCase().trim());
                            break;
                        case "attribute":
                            String[] attribs = line.split(",");
                            for(String attrib : attribs) {
                                attrib = attrib.trim();
                                switch(attrib) {
                                    case "static":
                                        ((Item)currentObject).setStatic(true);
                                        break;
                                    case "container":
                                        //does nothing for now
                                        break;
                                    case "hidden":
                                        ((Item)currentObject).setHidden(true);
                                        break;
                                    default:
                                        System.err.println("Bad attrib" + i);
                                }
                            }
                            break;
                        default:
                            System.err.println("Error: bad tag." + i);
                    }
                } catch(java.lang.ClassCastException e) {
                    System.out.println("Error: tag applied to wrong kind of object."+i);
                }
            }
        } catch(IOException e) {
            System.err.println("Error: file cannot be found");
        }
    }

}
