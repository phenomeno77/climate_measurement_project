package at.qe.skeleton.ui.controllers.rest;


import at.qe.skeleton.dao.RoomInformationDao;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.EmailService;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
public class LogDataController {

    @Autowired
    SensorService sensorService;

    @Autowired
    MeasurementService measurementService;

    @Autowired
    RoomService roomService;

    @Autowired
    EmailService emailService;

    @PostMapping("/save")
    void saveLogData(@RequestBody RoomInformationDao roomInformation) {
    	emailService.systemStartNotification();

        if (sensorService.sensorExists(roomInformation.getSensor().getSensorId())) {
            Sensor sensor = sensorService.loadSensor(roomInformation.getSensor().getSensorId());
            sensor.setLimits(roomInformation.getLimits());
            sensor.setRoom(roomService.loadRoomByName(roomInformation.getRoom().getRoomName()));
            sensorService.saveSensor(sensor);
        measurementService.mapMeasurementData(roomInformation);
        }
    }
}
