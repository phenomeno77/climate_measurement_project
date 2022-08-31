package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AuditLog;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.AuditLogService;
import at.qe.skeleton.services.DepartmentService;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("view")
public class SensorAddController {

    @Autowired
    SensorService sensorService;

    @Autowired
    AuditLogService auditLogService;

    @Autowired
    RoomService roomService;

    @Autowired
    DepartmentService departmentService;

    /**Set Services for the JUnit tests
     *
     * @param sensorService
     */
    public void setServices(SensorService sensorService,AuditLogService auditLogService,RoomService roomService,DepartmentService departmentService){
        this.sensorService = sensorService;
        this.auditLogService = auditLogService;
        this.roomService = roomService;
        this.departmentService = departmentService;
    }

    private Sensor sensor = new Sensor();

    /**
     * Getter
     * @return
     */
    public Sensor getSensor() {
        return sensor;
    }

    /**
     * Setter
     * @param sensor
     */
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void doSaveSensor(){
        AuditLog auditLog = new AuditLog();
        this.sensor = sensorService.saveSensor(sensor);
        auditLog.setEvent("New sensor added (ID: " + this.sensor.getSensorId() + ")");
        auditLogService.saveAuditLog(auditLog);
    }
}
