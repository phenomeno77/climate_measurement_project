package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Absence;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AbsenceRepository extends AbstractRepository<Absence, Integer>  {
    @Transactional
    @Modifying
    @Query("delete from Absence where absenceId = ?1")
    void deleteAbsence(int id);

    Absence findAbsenceByAbsenceId(int id);
}
