package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AuditLog;
import at.qe.skeleton.model.MailInterval;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.services.AuditLogService;
import at.qe.skeleton.services.EmailService;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import at.qe.skeleton.ui.beans.Validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
@Scope("view")
public class UserSettingsController {

    @Autowired
    private UserService userService;

    @Autowired
    Validations validations;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    AuditLogService auditLogService;

    private String messageInfo="";
    private String facesMessage="";

    /**
     * Set Services for the JUnit tests
     *
     * @param userService
     * @param sessionInfoBean
     * @param validations
     * @param passwordEncoder
     */
    public void setServices(UserService userService, SessionInfoBean sessionInfoBean, Validations validations,
                            PasswordEncoder passwordEncoder, AuditLogService auditLogService) {
        this.sessionInfoBean = sessionInfoBean;
        this.userService = userService;
        this.validations = validations;
        this.passwordEncoder = passwordEncoder;
        this.auditLogService = auditLogService;
    }

    /**
     * Attribute to encode the new generated password
     */
    private String password;
    private String passwordVerifier;
    private boolean resetPassword;

    private AuditLog auditLog;

    /**
     * Returns the current logged-in user
     *
     * @return
     */

    public Users getCurrentUser() {
        return userService.getAuthenticatedUser();
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordVerifier() {
        return passwordVerifier;
    }

    public void setPasswordVerifier(String passwordVerifier) {
        this.passwordVerifier = passwordVerifier;
    }

    public boolean isResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

    /**
     * Reset password if the entered password matches the users password
     *
     * @return
     */
    public boolean doResetPassword() {

        if( passwordEncoder.matches(password, getCurrentUser().getPassword())){
            setResetPassword(true);
        }
        else{
             messageInfo="Wrong Password";
             facesMessage="The password you have entered is wrong!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,messageInfo,
                    facesMessage));
        }
        setPassword("");
        return resetPassword;
    }

    /**
     * if the two passwords (in the UI boxes) are equal, change the password
     */
    public void doChangePassword() {

        if (getPassword().equals(getPasswordVerifier())) {

            getCurrentUser().setPassword(passwordEncoder.encode(getPasswordVerifier()));

            userService.saveCurrentUser();

            auditLog = new AuditLog();
            auditLog.setEvent("Password changed (ID: " + getCurrentUser().getUsername()+")");
            auditLogService.saveAuditLog(auditLog);

            setResetPassword(false);
            messageInfo="Password Changed";
            facesMessage="Your password has been successfully changed!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(messageInfo,
                    facesMessage));
           setPassword("");
        }
        else{
            messageInfo="Wrong Matching";
            facesMessage="The new passwords you have entered does not match! Please check your input again.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,messageInfo,
                    facesMessage));
        }
    }

    /**
     * Method to change currents user email
     */
    public void doChangeEmailAddress() {
        validations.checkValidEmail(getCurrentUser());

        if (validations.isValidEmail()) {
            userService.saveCurrentUser();
            auditLog = new AuditLog();
            auditLog.setEvent("Email changed (ID: " + getCurrentUser().getUsername()+")");
            auditLogService.saveAuditLog(auditLog);
            messageInfo="Email Changed";
            facesMessage="The email has been succesfully changed to: " + getCurrentUser().getEmail();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(messageInfo,
                    facesMessage));
        }
    }

    /**
     * Listener to save the value on change radio button
     *
     * @param event
     */
    public void selectMailIntervalListener(ValueChangeEvent event) {
        String interval = event.getNewValue().toString();
        getCurrentUser().setMailSettings(MailInterval.valueOf(interval));

        //after sending the first email, set when to send the next email, after one day, week or month
        if (!Objects.equals(interval, "OFF")) {
            getCurrentUser().setNextEmailPost(LocalDateTime.now().plusDays(MailInterval.valueOf(interval).getInterval()));
        }
        else{
            getCurrentUser().setNextEmailPost(null);
        }
        userService.saveCurrentUser();
        auditLog = new AuditLog();
        auditLog.setEvent("Mail frequency changed (ID: " + getCurrentUser().getUsername()+")");
        auditLogService.saveAuditLog(auditLog);

    }
}
