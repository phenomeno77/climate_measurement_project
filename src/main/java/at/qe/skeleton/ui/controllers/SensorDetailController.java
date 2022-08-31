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
public class SensorDetailController {

    @Autowired
    private SensorService sensorService;

    @Autowired
    RoomService roomService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    AuditLogService auditLogService;


    private Sensor sensor;

    private AuditLog auditLog;

    /**Set services for JUnit
     *
     * @param sensorService
     * @param auditLogService
     */
    public void setServices(SensorService sensorService,AuditLogService auditLogService,RoomService roomService,DepartmentService departmentService){
        this.sensorService = sensorService;
        this.auditLogService = auditLogService;
        this.roomService = roomService;
        this.departmentService = departmentService;
    }

    /**Getter
     *
     * @return
     */
    public Sensor getSensor() {
        return sensor;
    }

    /**Setter
     *
     * @param sensor
     */
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    /**Save sensor after editing
     */
    public void doSaveSensor() {
        auditLog = new AuditLog();
        auditLog.setEvent("Sensor (ID: " + this.sensor.getSensorId() + ") edited");
        auditLogService.saveAuditLog(auditLog);

        sensorService.saveSensor(sensor);

    }

    public void doRemoveSensor(){
        auditLog = new AuditLog();
        auditLog.setEvent("Sensor (ID: " + this.sensor.getSensorId() + ") deleted");
        auditLogService.saveAuditLog(auditLog);
        sensorService.deleteSensor(sensor);
        this.sensor = null;
    }

}
