package world;

public class Minotaur{
    private Room room;
    private Room prevRoom;
    private int attemptCount = 0;

    public Minotaur(Room start){
        room = start;
    }

    public Room getRoom() {
        return room;
    }

    public void move() {
        Direction[] directions = room.getConnectionDirs();
        if(prevRoom == null){
            prevRoom = room;
            int num = (int) (Math.random() * directions.length);
            room = room.getConnection(directions[num]);
        } else {
            Room nextRoom = prevRoom;
            while (prevRoom.equals(nextRoom)) {
                int num = (int) (Math.random() * directions.length);
                nextRoom = room.getConnection(directions[num]);
                attemptCount++;
                if(attemptCount == 10){
                    attemptCount = 0;
                    prevRoom = null;
                    move();
                }
            }
            prevRoom = room;
            room = nextRoom;
        }
    }

    public void setRoom(Room room) {
        prevRoom = this.room;
        this.room = room;
    }
}
