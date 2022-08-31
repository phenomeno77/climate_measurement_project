package at.qe.skeleton.dao;

public class RoomDao {
    private final String room;

    public RoomDao(String room) {
        this.room = room;

    }

    public String getRoomName() {
        return room;
    }
}
