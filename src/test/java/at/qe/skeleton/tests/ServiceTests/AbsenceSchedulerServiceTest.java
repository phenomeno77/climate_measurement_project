package at.qe.skeleton.tests.ServiceTests;

import at.qe.skeleton.model.Absence;
import at.qe.skeleton.model.AbsenceReason;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.services.AbsenceSchedulerService;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;

@SpringBootTest
@WebAppConfiguration
class AbsenceSchedulerServiceTest {

    @Autowired
    AbsenceSchedulerService absenceSchedulerService;

    @Autowired
    SessionInfoBean sessionInfoBean;

    @Autowired
    UserService userService;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testSaveAbsence(){
        Absence absence = new Absence();
        Users currentUsers = sessionInfoBean.getCurrentUser();

        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(13).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(15).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title");
        absence.setUser(currentUsers);

        absence = absenceSchedulerService.saveAbsence(absence);

        Assertions.assertEquals(1,absenceSchedulerService.getAllAbsences().size());

        absence = new Absence();
        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title2");
        absence.setUser(currentUsers);

        absence = absenceSchedulerService.saveAbsence(absence);

        Assertions.assertEquals(2,absenceSchedulerService.getAllAbsences().size());

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadAbsence(){
        Absence absence = new Absence();
        Users currentUsers = sessionInfoBean.getCurrentUser();

        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(13).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(15).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title");
        absence.setUser(currentUsers);

        absence = absenceSchedulerService.saveAbsence(absence);

        Assertions.assertEquals(1,absenceSchedulerService.getAllAbsences().size());

        Absence loadAbsence = absenceSchedulerService.loadAbsence(absence.getAbsenceId());

        Assertions.assertEquals(absence.getTitle(),loadAbsence.getTitle());
        Assertions.assertEquals(absence.getStartDate(),loadAbsence.getStartDate());
        Assertions.assertEquals(absence.getEndDate(),loadAbsence.getEndDate());
        Assertions.assertEquals(absence.getUser(),loadAbsence.getUser());
        Assertions.assertEquals(absence.getAbsenceId(),loadAbsence.getAbsenceId());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAbsenceByUser(){
        Absence absence = new Absence();
        Users currentUsers = sessionInfoBean.getCurrentUser();

        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(13).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(15).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title");
        absence.setUser(currentUsers);
        absence = absenceSchedulerService.saveAbsence(absence);

        absence = new Absence();
        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title2");
        absence.setUser(currentUsers);
        absence = absenceSchedulerService.saveAbsence(absence);

        Users currentUsers2 = userService.loadUser("user1");

        absence = new Absence();
        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title3");
        absence.setUser(currentUsers2);
        absence = absenceSchedulerService.saveAbsence(absence);

        absence = new Absence();
        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title4");
        absence.setUser(currentUsers2);
        absence = absenceSchedulerService.saveAbsence(absence);

        absence = new Absence();
        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title5");
        absence.setUser(currentUsers2);
        absence = absenceSchedulerService.saveAbsence(absence);

        Assertions.assertEquals(2,absenceSchedulerService.getAbsencesByUser(currentUsers).size());

        Assertions.assertEquals(3,absenceSchedulerService.getAbsencesByUser(currentUsers2).size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetALlAbsences(){
        Absence absence = new Absence();
        Users currentUsers = sessionInfoBean.getCurrentUser();

        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(13).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(15).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title");
        absence.setUser(currentUsers);
        absence = absenceSchedulerService.saveAbsence(absence);

        absence = new Absence();
        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title2");
        absence.setUser(currentUsers);
        absence = absenceSchedulerService.saveAbsence(absence);

        Users currentUsers2 = userService.loadUser("user1");

        absence = new Absence();
        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title3");
        absence.setUser(currentUsers2);
        absence = absenceSchedulerService.saveAbsence(absence);

        absence = new Absence();
        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title4");
        absence.setUser(currentUsers2);
        absence = absenceSchedulerService.saveAbsence(absence);

        absence = new Absence();
        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title5");
        absence.setUser(currentUsers2);
        absence = absenceSchedulerService.saveAbsence(absence);

        Assertions.assertEquals(5,absenceSchedulerService.getAllAbsences().size());
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeleteAbsence(){
        Absence absence = new Absence();
        Users currentUsers = sessionInfoBean.getCurrentUser();

        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(13).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(15).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title");
        absence.setUser(currentUsers);
        absence = absenceSchedulerService.saveAbsence(absence);

        Assertions.assertEquals(1,absenceSchedulerService.getAbsencesByUser(currentUsers).size());

        Absence absenceToDelete = absenceSchedulerService.loadAbsence(absence.getAbsenceId());

        absenceSchedulerService.deleteAbsence(absenceToDelete);

        Assertions.assertEquals(0,absenceSchedulerService.getAbsencesByUser(currentUsers).size());
    }
}
