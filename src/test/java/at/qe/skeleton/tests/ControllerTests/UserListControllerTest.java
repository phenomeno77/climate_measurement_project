package at.qe.skeleton.tests.ControllerTests;

import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.controllers.UserListController;
import org.joinfaces.test.mock.JsfMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;


@SpringBootTest
@WebAppConfiguration
class UserListControllerTest {

    public JsfMock jsfMock = new JsfMock();

    @Autowired
    private UserService userService;

    UserListController controller = new UserListController();

    @Autowired
    ApplicationContext applicationContext;


    @BeforeEach
    public void init() {
        jsfMock.init(applicationContext);
        controller.setServices(userService);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetALlUserAuthorizedUser() {

        Assertions.assertEquals(16,controller.getUsers().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testGetALlUserUnauthorizedUser() {

        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            Assertions.assertEquals(5, controller.getUsers().size());
        });
    }
}