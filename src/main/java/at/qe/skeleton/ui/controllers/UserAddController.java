package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.*;
import at.qe.skeleton.ui.beans.Validations;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.IOException;
import java.util.*;


@Component
@Scope("view")
public class UserAddController {

    @Autowired
    Validations validations;

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    RoomService roomService;

    @Autowired
    AuditLogService auditLogService;

    /**Set Services for the JUnit tests
     *
     * @param userService
     */

    public void setServices(UserService userService, PasswordEncoder passwordEncoder, Validations userAddValidations, EmailService emailService,
                            AuditLogService auditLogService, DepartmentService departmentService, RoomService roomService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.validations = userAddValidations;
        this.emailService = emailService;
        this.auditLogService = auditLogService;
        this.departmentService = departmentService;
        this.roomService = roomService;
    }

    public void setEmailService(EmailService emailService){
        this.emailService = emailService;
    }

    /**Attribute to encode the new generated password
     *
     */
    private String password;

    private Users user =new Users();

    public void doSaveUser() throws IOException {

        validations.checkValidUsername(getUser());
        validations.checkValidEmail(getUser());
        validations.checkRoomCapacity(getUser());
        validations.checkDepartmentManager(getUser());

        if(validations.isValidUsername()
                && validations.isValidEmail()
                    &&validations.isValidRoom()
                        &&validations.isValidDepartmentManager()) {
            user.setPassword(passwordEncoder.encode(password));

            AuditLog auditLog = new AuditLog();
            this.user = this.userService.saveUser(user);
            auditLog.setEvent("New user added (ID: " + user.getUsername() +")");
            auditLogService.saveAuditLog(auditLog);

            PrimeFaces.current().executeScript("PF('userAddDialog').hide()");
            emailService.sendMailUserAdded(this.user,getPassword());

                FacesContext.getCurrentInstance().getExternalContext().redirect("/users/users.xhtml");

            user = new Users();
        }
    }

    /**Setter & getters to add a new User*/
    public Users getUser() {
        return user;
    }

    public void setUser(Users users) {
        this.user = users;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Takes the {@link ValueChangeEvent} casts it to
     * a String and adds the corresponding UserRole
     *
     * @param event
     */
    public void selectRoleListener(ValueChangeEvent event)
    {
        if(event.getNewValue() != null) {
            String role = event.getNewValue().toString();
            user.setRoles(UserRole.valueOf(role));
        }
    }

    private String department;
    private String room;
    private List<String> departments;
    private List<String> rooms;

    /**Method where when the user selects a department,
     * only the rooms which are part of that department should
     * be listed below.
     */
    public void onDepartmentChange() {
        if (department != null && !"".equals(department)) {
            rooms = new ArrayList<>();
            for (Room r : roomService.getAllOffices()){
                if(Objects.equals(r.getDepartment().getDepartmentName(), department)) {
                    rooms.add(r.getRoomName());
                }
            }
        } else {
            rooms = new ArrayList<>();
        }
    }

    /**Setters and getters
     *
     * @return
     */
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
        this.user.setDepartment(departmentService.loadDepartmentByName(department));
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
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
        return rooms;
    }

    public void setRooms(List<String>  rooms) {
        this.rooms = rooms;
    }
}
