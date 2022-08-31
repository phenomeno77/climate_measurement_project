package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Scope("view")
public class SensorListController {

    @Autowired
    private SensorService sensorService;

    /**Set services for JUnit
     *
     * @param sensorService
     */
    public void setServices(SensorService sensorService){
        this.sensorService = sensorService;
    }

    /**Get all sensors
     *
     * @return
     */
    public List<Sensor> getSensors(){
        return new ArrayList<>(sensorService.getAllSensors());
    }

    /**Return sensors by room
     *
     * @param room
     * @return
     */
    public Collection<Sensor> getSensorsByRoom(Room room){
        return sensorService.getAllSensorsByRoom(room);
    }

}
