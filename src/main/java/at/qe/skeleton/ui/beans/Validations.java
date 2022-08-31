package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.Department;
import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Scope("request")
public class Validations {

    @Autowired
    UserService userService;

    private boolean isValidUsername;

    private boolean isValidEmail;

    private boolean isValidRoom;

    private boolean isValidDepartmentManager;

    /**Methods to set and get the boolean values of validations
     *
     * @return
     */
    public boolean isValidUsername() {
        return isValidUsername;
    }

    public void setValidUsername(boolean validUsername) {
        isValidUsername = validUsername;
    }

    public boolean isValidEmail() {
        return isValidEmail;
    }

    public void setValidEmail(boolean validEmail) {
        isValidEmail = validEmail;
    }

    public boolean isValidRoom() {
        return isValidRoom;
    }

    public void setValidRoom(boolean validRoom) {
        isValidRoom = validRoom;
    }

    public boolean isValidDepartmentManager() {
        return isValidDepartmentManager;
    }

    public void setValidDepartmentManager(boolean validDepartmentManager) {
        isValidDepartmentManager = validDepartmentManager;
    }

    private String messageInfo;
    private String facesMessage;


    public String getFacesMessage() {
        return facesMessage;
    }

    public String getMessageInfo() {
        return messageInfo;
    }


    /**Method to check if the username is valid or available
     *
     * @throws ValidatorException
     */
    public void checkValidUsername(Users users) throws ValidatorException {

        String newUsername = users.getUsername();
        Users newUsers = userService.loadUser(newUsername);

        //if the user exists in the database and is not removed
        if (userService.getUsers().contains(newUsers)) {

            messageInfo="Username Exists";
            facesMessage="The Username: " + newUsername + " already exists! Please choose another one.";

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(messageInfo,
                    facesMessage));
            setValidUsername(false);

        }
        else{
            setValidUsername(true);
        }
    }

    /**Method to check if the email is valid or available
     *
     * @throws ValidatorException
     */
    public void checkValidEmail(Users users) throws ValidatorException {

        String newEmail = users.getEmail();

        List<String> allEmails = new ArrayList<>();
        //search each user in all users (including the users with status Removed) and add each email into the array
            for (Users existingUsers : userService.getUsers()) {
                if (existingUsers.getEmail() != null
                        && !users.getUsername().equals(existingUsers.getUsername()) //exclude the current logged in user, editing its own email
                        && !Objects.equals(existingUsers.getEmail(), "")
                ) {
                    allEmails.add(existingUsers.getEmail());
                }
        }

        if(allEmails.contains(newEmail)){
            messageInfo="Email Exists";
            facesMessage="The E-mail: " + newEmail + " is already in use! Please choose another one.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,messageInfo,
                    facesMessage));
            setValidEmail(false);
        }
        else{
            setValidEmail(true);
        }
    }



    public void checkRoomCapacity(Users user){
        Room usersOffice = user.getOffice();

        if(usersOffice!=null &&
                !usersOffice.getEmployees().contains(user) &&
                    usersOffice.getEmployees().size() == usersOffice.getNumberOfSeats()){
            setValidRoom(false);
            messageInfo="Room Capacity exceeded";
            facesMessage="The room: " + user.getOffice().getRoomName() + " is full. Please select another room.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,messageInfo,
                    facesMessage));
        }
        else{
            setValidRoom(true);
        }
    }

    public void checkDepartmentManager(Users user) {
        Department department = user.getDepartment();

        if (user.getRoles() == null || !user.getRoles().equals(UserRole.MANAGER)) {
            setValidDepartmentManager(true);
        } else {
            if (department.getDepartmentManager() != null && user.getRoles().equals(UserRole.MANAGER)) {
                messageInfo = "Manager Exists";
                facesMessage = "The Department: " + user.getDepartment().getDepartmentName() + " already has a Manager.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, messageInfo,
                        facesMessage));
                setValidDepartmentManager(false);
            }

            if (department.getDepartmentManager() == null && user.getRoles().equals(UserRole.MANAGER)) {
                setValidDepartmentManager(true);
            }
        }
    }

}
