package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Department;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Scope("view")
public class DepartmentListController {

    @Autowired
    DepartmentService departmentService;

    /**Set Services for the JUnit tests
     *
     * @param departmentService
     */
    public void setServices(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public Collection<Department> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

    public Collection<Users> getUsersByDepartmentManager(){

        return departmentService.getUsersByDepartmentManager();
    }


}
