package world;

public class Minotaur{
    private Room room;
    private Room prevRoom;

    public Minotaur(Room start){
        room = start;
    }

    public Room getRoom() {
        return room;
    }

    public void move() {
        Direction[] directions = room.getConnectionDirs();
        Room nextRoom = prevRoom;
        while(prevRoom == nextRoom) {
            int num = (int) (Math.random() * directions.length);
            nextRoom = room.getConnection(directions[num])
        }
        prevRoom = room;
        room = nextRoom;
    }

    public void setRoom(Room room) {
        prevRoom = this.room;
        this.room = room;
    }
}
