package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.repositories.RoomRepository;
import at.qe.skeleton.repositories.SensorRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Service for accessing and manipulating sensors.
 */
@Transactional
@Service
public class SensorService {
    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private RoomRepository roomRepository;

    /**
     * Returns a collection of all sensors.
     *
     * @return all stored sensors
     */
    public Collection<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    /**
     Loads a single sensor identified by its id
     */
    public Sensor loadSensor(String sensorId) {
        return sensorRepository.findFirstBySensorId(sensorId);
    }

    /**
     * Saves the sensor.
     *
     * @param sensor the sensor to save
     * @return the updated/saved sensor
     */
    public Sensor saveSensor(Sensor sensor) {

        return sensorRepository.save(sensor);
    }

    /**
     * Deletes the sensor.
     *
     * @param sensor the sensor to delete
     */

    @Transactional
    public void deleteSensor(@NotNull Sensor sensor) {

        if(!sensor.getMeasurements().isEmpty()) {
            for (Measurement measurement : sensor.getMeasurements()) {
                measurement.setSensor(null);
                measurementRepository.save(measurement);
            }
            sensor.getMeasurements().removeAll(measurementRepository.findMeasurementsBySensor(sensor));
        }

        sensorRepository.deleteSensor(sensor.getSensorId());
    }


    /**
     * Return all sensors by room
     *
     * @param room
     * @return
     */
    public Collection<Sensor> getAllSensorsByRoom(Room room) {
        return room.getSensors();
    }

    /**
     * Checks if the sensor already exists in the db
     *
     * @param sensorId
     * @return
     */
    public boolean sensorExists(String sensorId){

       return sensorRepository.existsBySensorId(sensorId);
    }


}
