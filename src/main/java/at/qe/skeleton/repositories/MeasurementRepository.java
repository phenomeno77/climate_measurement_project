package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Sensor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface MeasurementRepository extends AbstractRepository<Measurement, Integer> {
    Collection<Measurement> findMeasurementsBySensor(Sensor sensor);

    @Transactional
    @Modifying
    @Query("delete from Measurement where measurementId = ?1")
    void deleteMeasurement(int id);

}
