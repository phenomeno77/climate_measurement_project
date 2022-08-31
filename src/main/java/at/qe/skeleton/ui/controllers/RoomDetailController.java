package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AuditLog;
import at.qe.skeleton.model.Department;
import at.qe.skeleton.model.Room;
import at.qe.skeleton.services.AuditLogService;
import at.qe.skeleton.services.DepartmentService;
import at.qe.skeleton.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("view")
public class RoomDetailController {

    @Autowired
    RoomService roomService;

    @Autowired
    AuditLogService auditLogService;

    @Autowired
    DepartmentService departmentService;


    private Room room;

    public void setServices(RoomService roomService,AuditLogService auditLogService,DepartmentService departmentService){
        this.auditLogService = auditLogService;
        this.roomService = roomService;
        this.departmentService = departmentService;
    }

    public void doSaveRoom(){
        this.roomService.saveRoom(room);
        AuditLog auditLog = new AuditLog();
        auditLog.setEvent("Room (Name: " + this.room.getRoomName() + ") edited");
        auditLogService.saveAuditLog(auditLog);
    }

    public void doRemoveRoom(){
        this.roomService.deleteRoom(room);
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }


    private List<String> departments;

    /**Setters and getters to change department
     *
     * @return
     */
    public String getDepartment() {
        return room.getDepartment().getDepartmentName();
    }

    public void setDepartment(String department) {
        this.room.setDepartment(departmentService.loadDepartmentByName(department));
    }

    public List<String> getDepartments() {
        departments = new ArrayList<>();
        for(Department d : departmentService.getAllDepartments()){
            departments.add(d.getDepartmentName());
        }
        return departments;
    }

    public void setDepartments(List<String>  departments) {
        this.departments = departments;
    }


}
