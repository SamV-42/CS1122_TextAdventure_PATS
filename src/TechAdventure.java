import game.*;
import parser.*;
import util.*;
import world.*;

import util.mixin.IdMixin;
import util.mixin.NamesMixin;
import util.mixin.InventoryMixin;
import util.mixin.ObjectionMixin;
import parser.command.DirectionCommand;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class TechAdventure {
    public static void main(String[] args) {
        Parser parser = new Parser();

        AnotherLoader ankj = new AnotherLoader();
        ankj.loadStuff();

        Player player = new Player("1", 893248092);
        player.setRoom(Registration.getOwnerByStr("room_id", "dungeon_hall_1"));


        Scanner inputScanner = new Scanner(System.in);
        while(true) {
            System.out.print("> ");
            String input = inputScanner.nextLine();
            System.out.println( parser.runPlayerInput(player, input) );
        }
    }
}
