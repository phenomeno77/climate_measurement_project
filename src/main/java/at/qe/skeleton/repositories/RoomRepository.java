package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Room;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RoomRepository extends AbstractRepository<Room, Integer> {
    Room findByRoomName(String roomName);
    @Transactional
    @Modifying
    @Query("delete from Room where roomId = ?1")
    void deleteById(int id);
}
