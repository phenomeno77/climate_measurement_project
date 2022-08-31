package at.qe.skeleton.tests;

import at.qe.skeleton.Tasks.UserSettingsTask;
import at.qe.skeleton.model.*;
import at.qe.skeleton.services.AbsenceSchedulerService;
import at.qe.skeleton.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootTest
@WebAppConfiguration
class UserSettingsTaskTest {

    @Autowired
    UserService userService;

    @Autowired
    UserSettingsTask userSettingsTask;

    @Autowired
    AbsenceSchedulerService absenceSchedulerService;

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testDoCheckUserAbsenceWithEmployee() {

        Users testUsers = userService.getAuthenticatedUser();

        Assertions.assertEquals(UserStatus.PRESENT, testUsers.getUserStatus());

        Absence absence = new Absence();
        absence.setStartDate(LocalDateTime.now().minusHours(1));
        absence.setEndDate(LocalDateTime.now().plusHours(1));
        absence.setUser(testUsers);
        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setTitle("VACATION");
        absenceSchedulerService.saveAbsence(absence);

        testUsers.getAbsences().add(absence);
        userService.saveCurrentUser();

        userSettingsTask.doCheckUserAbsence();

        testUsers = userService.loadUser(testUsers.getUsername());

        Assertions.assertEquals(UserStatus.VACATION, testUsers.getUserStatus());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoCheckUserAbsenceWithAdmin() {

        Users testUsers = userService.getAuthenticatedUser();

        Assertions.assertEquals(UserStatus.PRESENT, testUsers.getUserStatus());

        Absence absence = new Absence();
        absence.setStartDate(LocalDateTime.now().minusHours(1));
        absence.setEndDate(LocalDateTime.now().plusHours(1));
        absence.setUser(testUsers);
        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setTitle("VACATION");
        absenceSchedulerService.saveAbsence(absence);

        testUsers.getAbsences().add(absence);
        userService.saveCurrentUser();

        userSettingsTask.doCheckUserAbsence();

        testUsers = userService.loadUser(testUsers.getUsername());

        Assertions.assertEquals(UserStatus.VACATION, testUsers.getUserStatus());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoSendEmailToday() {
        LocalDateTime dateNow = LocalDateTime.now();
        String message = "Email sent successfully";
        Users users = userService.loadUser("admin");

        users.setMailSettings(MailInterval.DAILY);
        users.setNextEmailPost(dateNow);

        userService.saveUser(users);

        Assertions.assertEquals(message,userSettingsTask.doUserMailSend());
    }

}
