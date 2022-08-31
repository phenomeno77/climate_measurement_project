package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.AbsenceRepository;
import at.qe.skeleton.repositories.DepartmentRepository;
import at.qe.skeleton.repositories.RoomRepository;
import at.qe.skeleton.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Service for accessing and manipulating user data.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AbsenceRepository absenceRepository;

    @Autowired
    private DepartmentRepository departmentRepository;


    /**
     * Returns a collection of all users.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('FACILITY_MANAGER')")
    public Collection<Users> getAllUsers() {
        return userRepository.findAll();
    }

    /**Method to use for every user
     * see: validations, etc..
     * @return
     */
    public Collection<Users> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
//    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public Users loadUser(String username) {
        return userRepository.findFirstByUsername(username);
    }

    /**
     * Saves the user. This method will also set {@link Users#createDate} for new
     * entities or {@link Users#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link Users#createDate}
     * or {@link Users#updateUser} respectively.
     *
     * @param users the user to save
     * @return the updated user
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') || hasAuthority('FACILITY_MANAGER')")
    public Users saveUser(Users users) {
        if (users.isNew()) {
            if(users.getRoles()==null){
                users.setRoles(UserRole.EMPLOYEE);
                users.setCreateDate(new Date());
                users.setEnabled(true);
                users.setCreateUser(getAuthenticatedUser());
            }
            if (users.getDepartment().getDepartmentManager() == null && users.getRoles().equals(UserRole.MANAGER)) {
                users.setCreateDate(new Date());
                users.setEnabled(true);
                users.setCreateUser(getAuthenticatedUser());
                userRepository.save(users);

                users.getDepartment().setDepartmentManager(users);
                departmentRepository.save(users.getDepartment());
            }
            else{
                users.setCreateDate(new Date());
                users.setEnabled(true);
                users.setCreateUser(getAuthenticatedUser());
            }
            return userRepository.save(users);

        }
             if(users.getDepartment().getDepartmentManager() == null && users.getRoles().equals(UserRole.MANAGER)) {
            users.setUpdateDate(new Date());
            users.setUpdateUser(getAuthenticatedUser());
            userRepository.save(users);
            users.getDepartment().setDepartmentManager(users);
            departmentRepository.save(users.getDepartment());
            return userRepository.save(users);
        }
            if(users.getDepartment().getDepartmentManager() != null && users.getRoles().equals(UserRole.EMPLOYEE)){
                users.setUpdateDate(new Date());
                users.setUpdateUser(getAuthenticatedUser());
                userRepository.save(users);

                users.getDepartment().setDepartmentManager(null);
                departmentRepository.save(users.getDepartment());
                return userRepository.save(users);
            }

            else{
                users.setUpdateDate(new Date());
                users.setUpdateUser(getAuthenticatedUser());
            }
        return userRepository.save(users);
    }

    /**Method to update users absence.
     *
     */
    public void updateUser(Users users){
            userRepository.save(users);
    }

    /**Overloaded method to save the current logged in user
     *
     */
    public void saveCurrentUser(){
        userRepository.save(getAuthenticatedUser());
    }

    /**
     * Deletes the user.
     *
     * @param users the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') || hasAuthority('FACILITY_MANAGER')")
    @Transactional
    public void deleteUser(Users users) {
        users.setRoles(null);


        for(Absence absence : absenceRepository.findAll()){
            if(absence.getUser().equals(users)){
                absenceRepository.deleteAbsence(absence.getAbsenceId());
            }
        }

        if(users.getDepartment() != null){
        Department department = departmentRepository.findDepartmentByDepartmentId(users.getDepartment().getDepartmentId());
        if(department.getDepartmentManager() != null && department.getDepartmentManager().equals(users)) {
            department.setDepartmentManager(null);
        }
        }


        users.getAbsences().removeAll(absenceRepository.findAll().stream()
                .filter(absence -> absence.getUser().equals(users))
                .collect(Collectors.toSet()));

        for(Users user : getAllUsers()){
            if(user.getCreateUser()!=null && user.getCreateUser().equals(users)){
                user.setCreateUser(null);
            }
        }
        userRepository.save(users);
        userRepository.deleteById(users.getUsername());
    }

    public Users getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }


    public Collection<Users>findUsersByOffice(Room room){
        return userRepository.findUsersByOffice(room);
    }
    public Collection<Users>findUsersByAbsence(Absence absence){
        return userRepository.findUsersByAbsences(absence);
    }
    public Collection<Users>findUsersByDepartment(Department department){
        return userRepository.findUsersByDepartment(department);
    }
}
