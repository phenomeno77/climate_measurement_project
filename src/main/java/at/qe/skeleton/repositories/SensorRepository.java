package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Sensor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface SensorRepository extends AbstractRepository<Sensor, Integer> {
    Sensor findSensorByRoom(Room room);

    Sensor findFirstBySensorId(String sensorId);

    @Transactional
    @Modifying
    @Query("delete from Sensor where sensorId = ?1")
    void deleteSensor(String id);

    Collection<Sensor> findSensorsByRoom(Room room);
    Sensor findSensorBySensorId(int id);

    boolean existsBySensorId(String id);
}
