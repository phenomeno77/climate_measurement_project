package at.qe.skeleton.tests.ServiceTests;

import at.qe.skeleton.model.Department;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.services.DepartmentService;
import at.qe.skeleton.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collection;
import java.util.HashSet;

@SpringBootTest
@WebAppConfiguration
class DepartmentServiceTest {

    @Autowired
    DepartmentService departmentService;

    @Autowired
    UserService userService;

    @DirtiesContext
    @Test
    @WithMockUser(username = "hulk", authorities = {"MANAGER"})
    void testGetAllDepartmentsAuthorizedUserManager() {
        int allDepartments = 0;
        for(Department department : departmentService.getAllDepartments()){
            allDepartments++;
        }

        Assertions.assertEquals(allDepartments,departmentService.getAllDepartments().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllDepartmentsAuthorizedUserAdmin() {
        int allDepartments = 0;
        for(Department department : departmentService.getAllDepartments()){
            allDepartments++;
        }

        Assertions.assertEquals(allDepartments,departmentService.getAllDepartments().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testGetAllDepartmentsUnauthorizedUser() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            String username = "user1";
            Users users = userService.loadUser(username);
            Assertions.assertEquals(username, users.getUsername());
            departmentService.getAllDepartments();
            });
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "hulk", authorities = {"MANAGER"})
    void testLoadDepartmentByIDAuthorizedUser() {
        Department department = departmentService.loadDepartment(1);
        Assertions.assertEquals("IT",department.getDepartmentName());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testSaveDepartmentUnauthorizedUser() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            Department department = departmentService.loadDepartment(1);
            String username = "user1";
            Users users = userService.loadUser(username);
            Assertions.assertEquals(username, users.getUsername());
            departmentService.saveDepartment(department);
        });
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeleteDepartment() {
        Department departmentToDelete = departmentService.loadDepartment(1);
        int departments = departmentService.getAllDepartments().size();

        departmentService.deleteDepartment(departmentToDelete);

       Assertions.assertEquals(departments-1, departmentService.getAllDepartments().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadDepartmentByName() {
        String departmentName = "IT";
        Assertions.assertEquals(departmentName, departmentService.loadDepartmentByName(departmentName).getDepartmentName());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "hulk", authorities = {"MANAGER"})
    void testGetUsersByDepartmentManager() {
        Users users = userService.loadUser("hulk");
        Collection<Users> usersByDepartment = new HashSet<>();
        usersByDepartment.add(userService.loadUser("user1"));
        usersByDepartment.add(userService.loadUser("user2"));
        usersByDepartment.add(userService.loadUser("user3"));
        usersByDepartment.add(userService.loadUser("user4"));
        usersByDepartment.add(userService.loadUser("user5"));
        usersByDepartment.add(userService.loadUser("hulk"));

        Assertions.assertEquals(usersByDepartment.size(), departmentService.getUsersByDepartmentManager().size());
        Assertions.assertTrue(departmentService.getUsersByDepartmentManager().containsAll(usersByDepartment));
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testSaveDepartment() {
    Department department = departmentService.loadDepartment(1);
    department.setDepartmentName("Test");
    department = departmentService.saveDepartment(department);

    Assertions.assertEquals("Test",department.getDepartmentName());

    }

}
