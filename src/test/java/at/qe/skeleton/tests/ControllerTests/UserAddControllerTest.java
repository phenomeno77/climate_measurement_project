package at.qe.skeleton.tests.ControllerTests;

import at.qe.skeleton.model.Department;
import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.services.*;
import at.qe.skeleton.ui.beans.Validations;
import at.qe.skeleton.ui.controllers.UserAddController;
import org.joinfaces.test.mock.JsfMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;

@SpringBootTest
@WebAppConfiguration
class UserAddControllerTest {


    public JsfMock jsfMock = new JsfMock();

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuditLogService auditLogService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    Validations validations;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    RoomService roomService;

    @Autowired
    EmailService emailService;

    UserAddController controller = new UserAddController();

    @BeforeEach
    public void init() {
        jsfMock.init(applicationContext);

        controller.setServices(userService,passwordEncoder, validations,emailService,auditLogService,departmentService,roomService);

        EmailService emailServiceMocked = Mockito.mock(EmailService.class);
        controller.setEmailService(emailServiceMocked);
    }

    @AfterEach
    public void removeUserCreated(){
        Users users = userService.loadUser("TestUser");
        if(userService.getAllUsers().contains(users)){
            userService.deleteUser(users);
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testAddNewUserSuccessfully() throws IOException {

        int allUsers = userService.getAllUsers().size();
        Assertions.assertEquals(allUsers,userService.getAllUsers().size(),"Before adding new user, there are 4 users initialized in the DB");
        Room room = roomService.loadRoom(10);
        Department department = departmentService.loadDepartmentByName("IT");

        controller.getUser().setUsername("TestUser");
        controller.getUser().setEmail("test@mail.com");
        controller.setPassword("passwd");
        controller.getUser().setDepartment(department);
        controller.getUser().setOffice(room);

        controller.doSaveUser();

        Assertions.assertEquals(allUsers+1,userService.getAllUsers().size(),"After adding new user, there should be 5 users in the DB");

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testAddNewManagerSuccessfully() throws IOException {

        int allUsers = userService.getAllUsers().size();
        Assertions.assertEquals(allUsers,userService.getAllUsers().size());

        userService.deleteUser(userService.loadUser("hulk"));
        Assertions.assertEquals(allUsers-1,userService.getAllUsers().size());

        Room room = roomService.loadRoom(10);
        Department department = departmentService.loadDepartmentByName("IT");

        controller.getUser().setUsername("TestUser");
        controller.getUser().setEmail("test@mail.com");
        controller.getUser().setRoles(UserRole.MANAGER);
        controller.setPassword("passwd");
        controller.getUser().setDepartment(department);
        controller.getUser().setOffice(room);

        controller.doSaveUser();

        Assertions.assertEquals(allUsers,userService.getAllUsers().size());

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testCheckValidateUsernameFails() throws IOException {
        int allUsers = userService.getAllUsers().size();
        Assertions.assertEquals(allUsers,userService.getAllUsers().size(),"Before adding new user, there are 4 users initialized in the DB");
        Room room = roomService.loadRoom(10);


        String existingUserName = userService.loadUser("user1").getUsername();
        controller.getUser().setUsername(existingUserName);
        controller.getUser().setEmail("test@mail.com");
        controller.setPassword("passwd");
        controller.getUser().setOffice(room);
        controller.doSaveUser();

        Assertions.assertEquals(allUsers,userService.getAllUsers().size(),"Trying to add a user with an already existing username should fail");
        validations.checkValidUsername(controller.getUser());
        Assertions.assertFalse(validations.isValidUsername());
        Assertions.assertEquals("Username Exists", validations.getMessageInfo());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testCheckValidateUsernameSuccess() {

        controller.getUser().setUsername("TestUser");
        controller.getUser().setEmail("test@mail.com");
        controller.setPassword("passwd");

        validations.checkValidUsername(controller.getUser());
        Assertions.assertTrue(validations.isValidUsername());


    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testCheckValidateEmailFails() throws IOException {

        int allUsers = userService.getAllUsers().size();
        Assertions.assertEquals(allUsers,userService.getAllUsers().size(),"Before adding new user, there are 4 users initialized in the DB");
        Room room = roomService.loadRoom(10);

        String existingEmail = userService.loadUser("user1").getEmail();
        controller.getUser().setUsername("TestUser");
        controller.getUser().setEmail(existingEmail);
        controller.setPassword("passwd");
        controller.getUser().setOffice(room);
        controller.doSaveUser();

        Assertions.assertEquals(allUsers,userService.getAllUsers().size(),"Trying to add a user with an already existing email should fail");
        validations.checkValidEmail(controller.getUser());
        Assertions.assertFalse(validations.isValidEmail());
        Assertions.assertEquals("Email Exists", validations.getMessageInfo());

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testCheckValidateEmailSuccess() {

        controller.getUser().setUsername("TestUser");
        controller.getUser().setEmail("test@mail.com");
        controller.setPassword("passwd");

        validations.checkValidEmail(controller.getUser());
        Assertions.assertTrue(validations.isValidEmail());

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllDepartments(){
        int allDepartments = 0;
        for(Department department : departmentService.getAllDepartments()){
            allDepartments++;
        }

        Assertions.assertEquals(allDepartments,controller.getDepartments().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllRoomsNoDepartmentChosen(){

        Assertions.assertNull(controller.getRooms());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllRoomsWithDepartmentChosen(){

        controller.setDepartment("IT");
        controller.onDepartmentChange();

        Assertions.assertEquals(3,controller.getRooms().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testValidRoomFailureOnFullRoom() throws IOException {
        Room room = roomService.loadRoomByName("IT.OFFICE A");
        int usersSizeInRoom = room.getEmployees().size();
        room.setNumberOfSeats(usersSizeInRoom);
        roomService.saveRoom(room);

        Assertions.assertEquals(usersSizeInRoom,room.getEmployees().size());

        controller.getUser().setUsername("TestUser");
        controller.getUser().setEmail("test@mail.com");
        controller.setPassword("passwd");
        controller.getUser().setOffice(room);
        controller.doSaveUser();

        Assertions.assertFalse(validations.isValidRoom());
        Assertions.assertEquals("Room Capacity exceeded", validations.getMessageInfo());
        Assertions.assertEquals(usersSizeInRoom,room.getEmployees().size());
    }
}
