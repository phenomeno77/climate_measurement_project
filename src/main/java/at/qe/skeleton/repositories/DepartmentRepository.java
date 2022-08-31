package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Department;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface DepartmentRepository extends AbstractRepository<Department, Integer> {

    Department findByDepartmentName(String departmentName);

    @Transactional
    @Modifying
    @Query("delete from Department where departmentId = ?1")
    void deleteDepartment(int id);

    Department findDepartmentByDepartmentId(int id);
}
