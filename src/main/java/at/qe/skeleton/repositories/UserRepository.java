package at.qe.skeleton.repositories;

import at.qe.skeleton.model.*;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for managing {@link Users} entities.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
public interface UserRepository extends AbstractRepository<Users, String> {

    Users findFirstByUsername(String username);

    List<Users> findByUsernameContaining(String username);

    @Transactional
    @Modifying
    void deleteById(String username);

    @Query("SELECT u FROM Users u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName")
    List<Users> findByWholeNameConcat(@Param("wholeName") String wholeName);

    @Query("SELECT u FROM Users u WHERE :role = :role")
    List<Users> findByRole(@Param("role") UserRole role);

    Collection<Users> findUsersByOffice(Room room);

    Collection<Users> findUsersByAbsences(Absence absence);
    Collection<Users> findUsersByDepartment(Department department);
}
