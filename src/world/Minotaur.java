package world;

public class Minotaur extends Item {
    private Room room;
    private Room prevRoom;
    private int attemptCount = 0;

    public Minotaur(Room start){
        super("minotaur", "minotaur");
        this.setStatic(true);

        room = start;
        setRoom(room);
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
        } else if(directions.length != 0) {
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

            setRoom(nextRoom);
        }
    }

    public void setRoom(Room room) {
        if(prevRoom != null) { prevRoom.getInventoryMixin().remove(this); }
        prevRoom = this.room;
        if(room != null) { room.getInventoryMixin().add(this); }
        this.room = room;
    }
}
