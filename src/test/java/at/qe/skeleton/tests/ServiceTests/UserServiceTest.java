package at.qe.skeleton.tests.ServiceTests;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.AbsenceSchedulerService;
import at.qe.skeleton.services.DepartmentService;
import at.qe.skeleton.services.RoomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import at.qe.skeleton.services.UserService;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Some very basic tests for {@link UserService}.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@SpringBootTest
@WebAppConfiguration
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    AbsenceSchedulerService absenceSchedulerService;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeleteUser() {
        int allUsers = userService.getAllUsers().size();
        String username = "user1";
        Users adminUsers = userService.loadUser("admin");
        Assertions.assertNotNull(adminUsers, "Admin user could not be loaded from test data source");
        Users toBeDeletedUsers = userService.loadUser(username);
        Assertions.assertNotNull(toBeDeletedUsers, "User \"" + username + "\" could not be loaded from test data source");

        userService.deleteUser(toBeDeletedUsers);

        Assertions.assertEquals(allUsers-1, userService.getAllUsers().size(), "No user has been deleted after calling UserService.deleteUser");
        Users deletedUsers = userService.loadUser(username);
        Assertions.assertNull(deletedUsers, "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.loadUser");

        for (Users remainingUsers : userService.getAllUsers()) {
            Assertions.assertNotEquals(toBeDeletedUsers.getUsername(), remainingUsers.getUsername(), "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.getAllUsers");
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeleteUserWithAbsence() {
        Absence absence = new Absence();
        Users user1 = userService.loadUser("user1");

        absence.setAbsenceReason(AbsenceReason.VACATION);
        absence.setStartDate(LocalDateTime.now().withHour(13).withMinute(0).withSecond(0).withNano(0));
        absence.setEndDate(LocalDateTime.now().withHour(15).withMinute(0).withSecond(0).withNano(0));
        absence.setTitle("Testing Title");
        absence.setUser(user1);

        absenceSchedulerService.saveAbsence(absence);

        Assertions.assertEquals(1,absenceSchedulerService.getAllAbsences().size());


        int allUsers = userService.getAllUsers().size();
        String username = "user1";
        Users adminUsers = userService.loadUser("admin");
        Assertions.assertNotNull(adminUsers, "Admin user could not be loaded from test data source");
        Users toBeDeletedUsers = userService.loadUser(username);
        Assertions.assertNotNull(toBeDeletedUsers, "User \"" + username + "\" could not be loaded from test data source");

        userService.deleteUser(toBeDeletedUsers);

        Assertions.assertEquals(allUsers-1, userService.getAllUsers().size(), "No user has been deleted after calling UserService.deleteUser");
        Users deletedUsers = userService.loadUser(username);
        Assertions.assertNull(deletedUsers, "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.loadUser");

        for (Users remainingUsers : userService.getAllUsers()) {
            Assertions.assertNotEquals(toBeDeletedUsers.getUsername(), remainingUsers.getUsername(), "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.getAllUsers");
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testUpdateUser() {
        String username = "user1";
        Users adminUsers = userService.loadUser("admin");
        Assertions.assertNotNull(adminUsers, "Admin user could not be loaded from test data source");
        Users toBeSavedUsers = userService.loadUser(username);
        Assertions.assertNotNull(toBeSavedUsers, "User \"" + username + "\" could not be loaded from test data source");

        Assertions.assertNull(toBeSavedUsers.getUpdateUser(), "User \"" + username + "\" has a updateUser defined");
        Assertions.assertNull(toBeSavedUsers.getUpdateDate(), "User \"" + username + "\" has a updateDate defined");

        toBeSavedUsers.setEmail("changed-email@whatever.wherever");
        userService.saveUser(toBeSavedUsers);

        Users freshlyLoadedUsers = userService.loadUser("user1");
        Assertions.assertNotNull(freshlyLoadedUsers, "User \"" + username + "\" could not be loaded from test data source after being saved");
        Assertions.assertNotNull(freshlyLoadedUsers.getUpdateUser(), "User \"" + username + "\" does not have a updateUser defined after being saved");
        Assertions.assertEquals(adminUsers, freshlyLoadedUsers.getUpdateUser(), "User \"" + username + "\" has wrong updateUser set");
        Assertions.assertNotNull(freshlyLoadedUsers.getUpdateDate(), "User \"" + username + "\" does not have a updateDate defined after being saved");
        Assertions.assertEquals("changed-email@whatever.wherever", freshlyLoadedUsers.getEmail(), "User \"" + username + "\" does not have a the correct email attribute stored being saved");
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testCreateUser() {
        Users adminUsers = userService.loadUser("admin");
        Assertions.assertNotNull(adminUsers, "Admin user could not be loaded from test data source");
        Department department = departmentService.loadDepartmentByName("IT");
        String username = "newuser";
        String password = "passwd";
        String fName = "New";
        String lName = "User";
        String email = "new-email@whatever.wherever";
        String phone = "+12 345 67890";
        Users toBeCreatedUsers = new Users();
        toBeCreatedUsers.setUsername(username);
        toBeCreatedUsers.setPassword(password);
        toBeCreatedUsers.setEnabled(true);
        toBeCreatedUsers.setFirstName(fName);
        toBeCreatedUsers.setLastName(lName);
        toBeCreatedUsers.setEmail(email);
        toBeCreatedUsers.setPhone(phone);
        toBeCreatedUsers.setDepartment(department);
        toBeCreatedUsers.setRoles(UserRole.EMPLOYEE);

        userService.saveUser(toBeCreatedUsers);

        Users freshlyCreatedUsers = userService.loadUser(username);
        Assertions.assertNotNull(freshlyCreatedUsers, "New user could not be loaded from test data source after being saved");
        Assertions.assertEquals(username, freshlyCreatedUsers.getUsername(), "New user could not be loaded from test data source after being saved");
        Assertions.assertEquals(password, freshlyCreatedUsers.getPassword(), "User \"" + username + "\" does not have a the correct password attribute stored being saved");
        Assertions.assertEquals(fName, freshlyCreatedUsers.getFirstName(), "User \"" + username + "\" does not have a the correct firstName attribute stored being saved");
        Assertions.assertEquals(lName, freshlyCreatedUsers.getLastName(), "User \"" + username + "\" does not have a the correct lastName attribute stored being saved");
        Assertions.assertEquals(email, freshlyCreatedUsers.getEmail(), "User \"" + username + "\" does not have a the correct email attribute stored being saved");
        Assertions.assertEquals(phone, freshlyCreatedUsers.getPhone(), "User \"" + username + "\" does not have a the correct phone attribute stored being saved");
        Assertions.assertEquals(UserRole.EMPLOYEE, freshlyCreatedUsers.getRoles(), "User \"" + username + "\" does not have role EMPLOYEE");
        Assertions.assertNotNull(freshlyCreatedUsers.getCreateUser(), "User \"" + username + "\" does not have a createUser defined after being saved");
        Assertions.assertEquals(adminUsers, freshlyCreatedUsers.getCreateUser(), "User \"" + username + "\" has wrong createUser set");
        Assertions.assertNotNull(freshlyCreatedUsers.getCreateDate(), "User \"" + username + "\" does not have a createDate defined after being saved");
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testExceptionForEmptyUsername() {
        Assertions.assertThrows(org.springframework.orm.jpa.JpaSystemException.class, () -> {
            Users adminUsers = userService.loadUser("admin");
            Assertions.assertNotNull(adminUsers, "Admin user could not be loaded from test data source");

            Users toBeCreatedUsers = new Users();
            Department department = departmentService.loadDepartmentByName("IT");
            toBeCreatedUsers.setDepartment(department);
            userService.saveUser(toBeCreatedUsers);
        });
    }
    @DirtiesContext
    @Test
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    void testUnauthorizedLoadUsers() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            for (Users users : userService.getAllUsers()) {
                Assertions.fail("Call to userService.getAllUsers should not work without proper authorization");
            }
        });
    }
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testAuthorizedLoadUser() {
        String username = "user1";
        Users users = userService.loadUser(username);
        Assertions.assertEquals(username, users.getUsername(), "Call to userService.loadUser returned wrong user");
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testUnauthorizedSaveUser() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            String username = "user1";
            Users users = userService.loadUser(username);
            Assertions.assertEquals(username, users.getUsername(), "Call to userService.loadUser returned wrong user");
            userService.saveUser(users);
        });
    }
    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testUnauthorizedDeleteUser() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            Users users = userService.loadUser("user1");
            Assertions.assertEquals("user1", users.getUsername(), "Call to userService.loadUser returned wrong user");
            userService.deleteUser(users);
        });
    }
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testAuthorizedDeleteManager() {

            Users users = userService.loadUser("hulk");
            userService.deleteUser(users);
            Assertions.assertFalse(userService.getUsers().contains(users));
    }
    @DirtiesContext
    @Test
    @WithMockUser(username = "spiderman", authorities = {"FACILITY_MANAGER"})
    void testAuthorizedDeleteAdmin() {

        Users users = userService.loadUser("admin");
        userService.deleteUser(users);
        Assertions.assertFalse(userService.getUsers().contains(users));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testFindUsersByDepartment() {
        int actualUsersByDepartment=0;
      int usersByDepartment = userService.findUsersByDepartment(departmentService.loadDepartment(1)).size();
      for(Users users : userService.findUsersByDepartment(departmentService.loadDepartment(1))){
          actualUsersByDepartment++;
      }

      Assertions.assertEquals(usersByDepartment,actualUsersByDepartment);
    }
}