package at.qe.skeleton.tests.ControllerTests;

import at.qe.skeleton.model.Department;
import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.services.AuditLogService;
import at.qe.skeleton.services.DepartmentService;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import at.qe.skeleton.ui.beans.Validations;
import at.qe.skeleton.ui.controllers.UserDetailController;
import org.joinfaces.test.mock.JsfMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
class UserDetailControllerTest {


    public JsfMock jsfMock = new JsfMock();

    @Autowired
    private UserService userService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    AuditLogService auditLogService;

    @Autowired
    Validations validations;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    RoomService roomService;

    UserDetailController controller = new UserDetailController();


    @BeforeEach
    public void init() {
        jsfMock.init(applicationContext);
        controller.setServices(userService, validations,auditLogService,departmentService,roomService);

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testSettersGettersUser() {
        Users users1 = userService.loadUser("user1");
        controller.setUser(users1);

        Assertions.assertEquals("user1",controller.getUser().getUsername());

        Users users2 = userService.loadUser("user2");
        controller.setUser(users2);

        Assertions.assertEquals("user2",controller.getUser().getUsername());

        controller.getUser().setUsername("TestUsername");


        Assertions.assertEquals("TestUsername",controller.getUser().getUsername());
    }



    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"ADMIN"})
    void testDoReloadUser() {
        Users users1 = userService.loadUser("user1");
        controller.setUser(users1);
        controller.getUser().setUsername("something");
        Assertions.assertNotEquals("user1",controller.getUser().getUsername());

        users1 = userService.loadUser("user1");
        controller.setUser(users1);
        controller.doReloadUser();
        Assertions.assertEquals("user1", users1.getUsername());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"ADMIN"})
    void testUserRoles() {
        Users users1 = userService.loadUser("user1");
        controller.setUser(users1);
        Assertions.assertEquals(UserRole.EMPLOYEE,UserRole.valueOf(controller.getRole()));

        controller.setRole("MANAGER");
        Assertions.assertEquals(UserRole.MANAGER,UserRole.valueOf(controller.getRole()));

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoDeleteUser() {
        int allUsers = userService.getAllUsers().size();
        Users users1 = userService.loadUser("user1");
        controller.setUser(users1);
        controller.doDeleteUser();

        Assertions.assertEquals(allUsers-1,userService.getAllUsers().size());

        Users users2 = userService.loadUser("user2");
        controller.setUser(users2);
        controller.doDeleteUser();

        Assertions.assertEquals(allUsers-2,userService.getAllUsers().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testIsValidEmail() {
        Users users1 = userService.loadUser("user1");
        String alreadyExistingMail = users1.getEmail();

        Users users2 = userService.loadUser("user2");
        controller.setUser(users2);
        controller.getUser().setEmail(alreadyExistingMail);
        validations.checkValidEmail(controller.getUser());

        String messageInfo="Email Exists";
        String facesMessage="The E-mail: " + controller.getUser().getEmail() + " is already in use! Please choose another one.";

        Assertions.assertEquals(messageInfo, validations.getMessageInfo());
        Assertions.assertEquals(facesMessage, validations.getFacesMessage());

        String validEMail = "testValidEMail@mail.com";
        controller.getUser().setEmail(validEMail);
        validations.checkValidEmail(controller.getUser());

        Assertions.assertEquals(validEMail,controller.getUser().getEmail());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoSaveUser() {
        Users users1 = userService.loadUser("admin");
        controller.setUser(users1);
        controller.getUser().setFirstName("TestUser");

        controller.doSaveUser();

        Assertions.assertEquals("TestUser",userService.loadUser("admin").getFirstName());
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetDepartment() {
        Users users1 = userService.loadUser("admin");
        controller.setUser(users1);
        String departmentName = controller.getDepartment();

        Assertions.assertEquals(controller.getDepartment(),departmentName);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllDepartments() {
        Users users1 = userService.loadUser("admin");
        int allDepartments = departmentService.getAllDepartments().size();

        Assertions.assertEquals(controller.getDepartments().size(),allDepartments);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllRooms() {
        Users users1 = userService.loadUser("admin");
        controller.setUser(users1);
        int allRooms = controller.getUser().getDepartment().getRooms().size();

        Assertions.assertEquals(controller.getRooms().size(),allRooms);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testAddNewManagerSuccessfully() {

        int allUsers = userService.getAllUsers().size();
        Assertions.assertEquals(allUsers,userService.getAllUsers().size());

        userService.deleteUser(userService.loadUser("hulk"));
        Assertions.assertEquals(allUsers-1,userService.getAllUsers().size());

        Department department = departmentService.loadDepartmentByName("IT");

        Users user9 = userService.loadUser("user9");
        controller.setUser(user9);

        controller.getUser().setRoles(UserRole.MANAGER);
        controller.getUser().setDepartment(department);
        controller.doSaveUser();

        Assertions.assertEquals(UserRole.MANAGER, controller.getUser().getRoles());

        Assertions.assertEquals(department.getDepartmentManager().getUsername(),controller.getUser().getUsername());

        Assertions.assertEquals(allUsers-1,userService.getAllUsers().size());

    }

}
