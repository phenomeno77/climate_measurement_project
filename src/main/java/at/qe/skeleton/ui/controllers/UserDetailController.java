package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.AuditLogService;
import at.qe.skeleton.services.DepartmentService;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.beans.Validations;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Controller for the user detail view.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("view")
public class UserDetailController{

    @Autowired
    private UserService userService;

    @Autowired
    Validations validations;

    @Autowired
    AuditLogService auditLogService;

    /**Set Services for the JUnit tests
     *
     * @param userService
     * @param validations
     */
    public void setServices(UserService userService, Validations validations, AuditLogService auditLogService,
                            DepartmentService departmentService, RoomService roomService) {
        this.userService = userService;
        this.validations = validations;
        this.auditLogService = auditLogService;
        this.departmentService = departmentService;
        this.roomService = roomService;
    }

    /**
     * Attribute to cache the currently displayed user
     */
    private Users user;

    private AuditLog auditLog;


    /**
     * Sets the currently displayed user and reloads it form db. This user is
     * targeted by any further calls of
     * {@link #doReloadUser()}, {@link #} and
     * {@link #doDeleteUser()}.
     *
     * @param users
     */
    public void setUser(Users users) {
        this.user = users;
        doReloadUser();
    }

    /**
     * Returns the currently displayed user.
     *
     * @return
     */
    public Users getUser() {
        return user;
    }

    /**
     * Action to force a reload of the currently displayed user.
     */
    public void doReloadUser() {
        user = userService.loadUser(user.getUsername());
    }

    /**
     * Action to save the currently displayed user.
     * */
    public void doSaveUser() {

        validations.checkValidEmail(this.user);
        validations.checkRoomCapacity(this.user);
        validations.checkDepartmentManager(this.user);

        if (validations.isValidEmail()
                && validations.isValidRoom()
                    && validations.isValidDepartmentManager()) {

            user = this.userService.saveUser(user);

            auditLog = new AuditLog();
            auditLog.setEvent("User (ID: " + user.getUsername() +") edited");
            auditLogService.saveAuditLog(auditLog);
            PrimeFaces.current().executeScript("PF('userEditDialog').hide()");
        }
    }

    /**
     * Action to delete the currently displayed user.
     */
    public void doDeleteUser() {
        auditLog = new AuditLog();
        auditLog.setEvent("User deleted (ID: " + user.getUsername() +")");
        auditLogService.saveAuditLog(auditLog);
        this.userService.deleteUser(user);
        user = null;
    }

    public String getRole(){

        return user.getRoles().name();
    }

    public void setRole(String role) {
        /**
         * Getter with foreach loop to first display all current roles.
         * With each click or unclick on check box, setter will update the roles
         */
        if(!Objects.equals(role, "")) {
            this.user.setRoles(UserRole.valueOf(role));
        }
        else{
            this.user.setRoles(user.getRoles());
        }
    }


    private String department;
    private List<String> departments;
    private List<String> rooms = new ArrayList<>();

    @Autowired
    DepartmentService departmentService;

    @Autowired
    RoomService roomService;

    /**Method where when the user selects a department,
     * only the rooms which are part of that department should
     * be listed below.
     */
    public void onDepartmentChange() {
        if (user.getDepartment() == null && "".equals(department)) {
            rooms = new ArrayList<>();
        }
    }

    /**Setters and getters
     *
     * @return
     */
    public String getDepartment() {
        if(user.getDepartment() == null){
            return "Select Department";
        }
        return user.getDepartment().getDepartmentName();
    }

    public void setDepartment(String department) {
        this.department = department;
        this.user.setDepartment(departmentService.loadDepartmentByName(department));
    }

    public String getRoom() {
        if(user.getOffice() == null){
            return "Select Room";
        }
        return user.getOffice().getRoomName();
    }

    public void setRoom(String room) {
        this.user.setOffice(roomService.loadRoomByName(room));
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

    public List<String>  getRooms() {
        rooms.clear();
        if(user.getDepartment() != null) {
            for (Room r : roomService.getAllOffices()) {
                if (Objects.equals(r.getDepartment().getDepartmentName(), user.getDepartment().getDepartmentName())) {
                    rooms.add(r.getRoomName());
                }
            }
        }
      else {
            rooms = new ArrayList<>();
        }
        return rooms;
    }

    public void setRooms(List<String>  rooms) {
        this.rooms = rooms;
    }
}

