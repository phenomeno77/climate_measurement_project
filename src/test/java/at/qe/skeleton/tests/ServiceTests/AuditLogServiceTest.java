package at.qe.skeleton.tests.ServiceTests;

import at.qe.skeleton.model.AuditLog;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.services.AuditLogService;
import at.qe.skeleton.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@WebAppConfiguration
public class AuditLogServiceTest {

    @Autowired
    AuditLogService auditLogService;

    @Autowired
    UserService userService;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoSaveAuditLog() {
        AuditLog auditLog = new AuditLog();

        auditLog.setTimeStamp(new Date());
        auditLog.setUser("admin");
        auditLog.setEvent("Test");
        auditLogService.saveAuditLog(auditLog);

        Assertions.assertEquals(1,auditLogService.getAllAuditLogs().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoSaveAuditLogout() {
        AuditLog auditLog = new AuditLog();

        auditLog.setTimeStamp(new Date());
        auditLog.setUser("admin");
        auditLog.setEvent("Test");

        auditLogService.saveAuditLogLogout(auditLog,"admin");

        Assertions.assertEquals(1,auditLogService.getAllAuditLogs().size());
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "spiderman", authorities = {"FACILITY_MANAGER"})
    void testGetAuditLogAuthorizedUserFacilityManager() {
        AuditLog auditLog = new AuditLog();

        auditLog.setTimeStamp(new Date());
        auditLog.setUser("spiderman");
        auditLog.setEvent("Test");

        auditLogService.saveAuditLogLogout(auditLog,"admin");

        Assertions.assertEquals(1,auditLogService.getAllAuditLogs().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user5", authorities = {"EMPLOYEE"})
    void testGetAuditLogUnauthorizedUserEmployee() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
           auditLogService.getAllAuditLogs();
        });
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user5", authorities = {"MANAGER"})
    void testGetAuditLogUnauthorizedUserManager() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            auditLogService.getAllAuditLogs();
        });
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAuditLogByDepartmentAuthorizedUser() {

        int allLogsByDepartment = auditLogService.getAllAuditLogsByDepartment().size();

        Users manager = userService.loadUser("hulk");
        List<AuditLog> auditLogs = new ArrayList<>();

        for(Users users : manager.getOffice().getEmployees()){
            for(AuditLog auditLog : auditLogService.getAllAuditLogs()){
                if(auditLog.getUser().equals(users.getUsername())){
                    auditLogs.add(auditLog);
                }
            }
        }

        Assertions.assertEquals(allLogsByDepartment,auditLogs.size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAuditLogByDepartmentAuthorizedUserIfStatement() {

        Users manager = userService.loadUser("hulk");
        int allLogsByDepartment = 0;

        Date now = new Date();
        AuditLog newAudit = new AuditLog();
        newAudit.setEvent("Test");
        newAudit.setUser("hulk");
        newAudit.setTimeStamp(now);
        auditLogService.saveAuditLog(newAudit);

        List<AuditLog> auditLogs = auditLogService.getAllAuditLogsByDepartment();

        for(AuditLog auditLog : auditLogs){
            Assertions.assertNotNull(auditLog.getUser());
            Assertions.assertNotNull(auditLog.getTimeStamp());
            Assertions.assertNotNull(auditLog.getEvent());
            allLogsByDepartment++;
        }

        Assertions.assertEquals(allLogsByDepartment,auditLogService.getAllAuditLogsByDepartment().size());





    }



}
