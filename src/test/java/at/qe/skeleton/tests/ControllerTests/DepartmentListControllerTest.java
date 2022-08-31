package at.qe.skeleton.tests.ControllerTests;

import at.qe.skeleton.model.Department;
import at.qe.skeleton.services.DepartmentService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import at.qe.skeleton.ui.controllers.DepartmentListController;
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
class DepartmentListControllerTest {

    public JsfMock jsfMock = new JsfMock();


    @Autowired
    DepartmentService departmentService;

    @Autowired
    ApplicationContext applicationContext;

    DepartmentListController controller = new DepartmentListController();

    @BeforeEach
    public void init() {
        jsfMock.init(applicationContext);
        controller.setServices(departmentService);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllDepartments() {
        int allDepartments = 0;
        for(Department department : departmentService.getAllDepartments()){
            allDepartments++;
        }

        Assertions.assertEquals(allDepartments,controller.getAllDepartments().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "hulk", authorities = {"MANAGER"})
    void testGetUserByDepartmentManager() {
        int usersByDepartment = departmentService.getUsersByDepartmentManager().size();
        Assertions.assertEquals(usersByDepartment,controller.getUsersByDepartmentManager().size());
    }
}
