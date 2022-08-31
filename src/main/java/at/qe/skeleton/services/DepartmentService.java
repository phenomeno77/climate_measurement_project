package at.qe.skeleton.services;

import at.qe.skeleton.model.Department;
import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.repositories.DepartmentRepository;
import at.qe.skeleton.repositories.RoomRepository;
import at.qe.skeleton.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

/**
 * Service for accessing and manipulating department data.
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    /**
     * Returns a collection of all departments.
     *
     * @return all stored departments
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('FACILITY_MANAGER') ")
    public Collection<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    /**
     * Loads a single departments identified by its id.
     *
     * @param id the id of the given departments to search for
     * @return the departments with the given id
     */
    public Department loadDepartment(int id) {
        return departmentRepository.findById(id).orElse(null);
    }

    /**
     * Saves the department.
     *
     * @param department the department to save
     * @return the updated/saved department
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FACILITY_MANAGER')")
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    /**
     * Deletes the department.
     *
     * @param department the department to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteDepartment(Department department) {
        Department departmentToDelete = loadDepartment(department.getDepartmentId());
                for(Users users : userRepository.findUsersByDepartment(departmentToDelete)){
                    users.setDepartment(null);
                    userRepository.save(users);
                }

                for(Room room : departmentRepository.findDepartmentByDepartmentId(departmentToDelete.getDepartmentId()).getRooms()){
                    room.setDepartment(null);
                    roomRepository.save(room);
                }

            departmentRepository.deleteDepartment(departmentToDelete.getDepartmentId());
    }

    /**
     * Load department by name
     * @param departmentName
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('FACILITY_MANAGER')")
    public Department loadDepartmentByName(String departmentName){
        return departmentRepository.findByDepartmentName(departmentName);
    }

    /**
     * Return a list of users from department of managers.
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('FACILITY_MANAGER')")
    public Collection<Users> getUsersByDepartmentManager(){
        Collection<Users> usersByDepartmentSet = new HashSet<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users currentUsers = userRepository.findFirstByUsername(auth.getName());

        String departmentName = currentUsers.getDepartment().getDepartmentName();
        Department department = loadDepartmentByName(departmentName);

        for(Users users : userRepository.findAll()){
            if(department.equals(users.getDepartment())){
                usersByDepartmentSet.add(users);
            }
        }
        return usersByDepartmentSet;
    }

}
