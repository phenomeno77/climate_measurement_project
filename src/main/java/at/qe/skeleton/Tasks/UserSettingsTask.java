package at.qe.skeleton.Tasks;

import at.qe.skeleton.model.Absence;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.model.UserStatus;
import at.qe.skeleton.services.EmailService;
import at.qe.skeleton.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserSettingsTask {

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    /**Check for each user the absence and set its status to the corresponding status
     *
     */
    public void doCheckUserAbsence(){
        LocalDateTime ldtNow = LocalDateTime.now();
        for(Users users : userService.getUsers()){
            if(users.getAbsences().isEmpty()){
                users.setUserStatus(UserStatus.PRESENT);
            }
            else {
                for (Absence absence : users.getAbsences()) {
                    if (absence.getStartDate().isBefore(ldtNow)
                            && absence.getEndDate().isAfter(ldtNow)) {
                        users.setUserStatus(UserStatus.valueOf(absence.getAbsenceReason().name()));
                    } else {
                        users.setUserStatus(UserStatus.PRESENT);
                    }
                }
            }
            userService.updateUser(users);
        }
    }

    /**Method to check the interval of the subscribed users.
     *
     */
    public String doUserMailSend() {

        for(Users users : userService.getUsers()){
            if (!Objects.equals(users.getMailSettings().name(), "OFF")
            && users.getNextEmailPost().getDayOfYear() == LocalDateTime.now().getDayOfYear()) {
                emailService.sendMailNotification(users);
                users.setNextEmailPost(LocalDateTime.now().plusDays(users.getMailSettings().getInterval()));
                userService.updateUser(users);
                return "Email sent successfully";
            }
        }
        return "";
    }

}
