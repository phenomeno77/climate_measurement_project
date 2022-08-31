package at.qe.skeleton.tests.ControllerTests;

import at.qe.skeleton.model.Users;
import at.qe.skeleton.services.AuditLogService;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import at.qe.skeleton.ui.beans.Validations;
import at.qe.skeleton.ui.controllers.UserSettingsController;
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
public class UserSettingsControllerTest {

    public JsfMock jsfMock = new JsfMock();

    @Autowired
    private UserService userService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    Validations validations;

    @Autowired
    AuditLogService auditLogService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    UserSettingsController controller = new UserSettingsController();


    @BeforeEach
    public void init() {
        jsfMock.init(applicationContext);
        controller.setServices(userService,sessionInfoBean, validations,passwordEncoder,auditLogService);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user2", authorities = {"USER"})
    void testGetCurrentUser2() {
        Assertions.assertEquals("user2",controller.getCurrentUser().getUsername());
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"MANAGER"})
    void testGetCurrentUser() {
        Assertions.assertEquals("user1",controller.getCurrentUser().getUsername());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoPasswordResetSuccess() {
        String typedInPassword = "passwd";

        controller.setPassword(typedInPassword);

        Assertions.assertTrue(controller.doResetPassword());

    }



    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoPasswordChangeSuccess() {
        String typedInPassword = "passwd";
        String newPassword = "newPasswd";
        String matchingPassword = typedInPassword;

        controller.setPassword(typedInPassword);

        Assertions.assertTrue(controller.doResetPassword());

        controller.setPassword(newPassword);
        controller.setPasswordVerifier(matchingPassword);

        controller.doChangePassword();

        Assertions.assertTrue(passwordEncoder.matches(matchingPassword, sessionInfoBean.getCurrentUser().getPassword()));
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoPasswordResetFailure() {
        String notMatchingPassword = "oldPasswd";
        controller.setPassword(notMatchingPassword);

        Assertions.assertFalse(controller.doResetPassword());
        Assertions.assertFalse(controller.isResetPassword());
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoPasswordChangeFailure() {
        String typedInPassword = "passwd";
        String newPassword = "newPasswd";
        String notMatchingPassword = "oldPasswd";

        controller.setPassword(typedInPassword);

        Assertions.assertTrue(controller.doResetPassword());

        controller.setPassword(newPassword);
        controller.setPasswordVerifier(notMatchingPassword);

        controller.doChangePassword();

        Assertions.assertFalse(passwordEncoder.matches(notMatchingPassword, sessionInfoBean.getCurrentUser().getPassword()));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"ADMIN"})
    void testChangeEmailAddressFailure() {
        String currentUserEmail = controller.getCurrentUser().getEmail();

        Users anotherUsers = userService.loadUser("user2");
        String anotherUserEmail = anotherUsers.getEmail();

        controller.getCurrentUser().setEmail(anotherUserEmail);
        controller.doChangeEmailAddress();

        Assertions.assertNotEquals(currentUserEmail,anotherUserEmail,"If the email address already exists, it should not save the user");
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testChangeEmailAddressSuccess() {

        String randomNonExistingMail = "something_random@mail.com";

        sessionInfoBean.getCurrentUser().setEmail(randomNonExistingMail);
        controller.doChangeEmailAddress();

        Assertions.assertEquals(sessionInfoBean.getCurrentUser().getEmail(),randomNonExistingMail,"The email does not exist, so the user can change its email to the new one.");

    }

}
