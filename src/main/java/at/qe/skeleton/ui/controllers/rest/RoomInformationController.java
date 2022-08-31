package at.qe.skeleton.ui.controllers.rest;


import at.qe.skeleton.dao.RoomInformationDao;

import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/roomInformation")
public class RoomInformationController {

    @Autowired
    SensorService sensorService;

    @Autowired
    MeasurementService measurementService;

    @Autowired
    RoomService roomService;

    @PostMapping("/save")
    void addRoom(@RequestBody RoomInformationDao roomInformation) {


        if (sensorService.sensorExists(roomInformation.getSensor().getSensorId())) {
            Sensor sensor = sensorService.loadSensor(roomInformation.getSensor().getSensorId());
            sensor.setLimits(roomInformation.getLimits());
            sensor.setRoom(roomService.loadRoomByName(roomInformation.getRoom().getRoomName()));
            sensorService.saveSensor(sensor);
            measurementService.mapMeasurementData(roomInformation);

        }
    }


}