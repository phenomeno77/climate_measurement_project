package at.qe.skeleton.ui.controllers.rest;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @Autowired
    SensorService sensorService;

    @Autowired
    RoomService roomService;
    @PostMapping("/add")
    Sensor addSensor(@RequestBody Sensor sensor) {
        Room room = roomService.loadRoom(10);
        sensor.setRoom(room);
        sensor.setRoom(sensor.getRoom());
        return sensorService.saveSensor(sensor);
    }
}
