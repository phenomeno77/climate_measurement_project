package at.qe.skeleton.services;

import at.qe.skeleton.model.Absence;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.repositories.AbsenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AbsenceSchedulerService {

    @Autowired
    AbsenceRepository absenceRepository;

    @Autowired
    UserService userService;


    /**Method to save each absence from event
     *
     * @param absence
     * @return
     */
    public Absence saveAbsence(Absence absence) {
           return absenceRepository.save(absence);
    }

    /**Load absence by given id
     *
     * @param id
     * @return
     */
    public Absence loadAbsence(int id){
        return absenceRepository.findById(id).orElse(null);
    }

    /**get the set of all absences by user
     *
     * @param users
     * @return
     */
    public Collection<Absence> getAbsencesByUser(Users users){

        return absenceRepository.findAll().stream()
                .filter(absence -> absence.getUser().equals(users))
                .collect(Collectors.toSet());
    }

    /**get all absences from the repository
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Absence> getAllAbsences(){

        return absenceRepository.findAll();
    }


    /**method to remove an absence from the scheduler
     *
     * @param absence
     */
    @Transactional
    public void deleteAbsence(Absence absence) {
        Absence absence2 = absenceRepository.findAbsenceByAbsenceId(absence.getAbsenceId());

        absence2.setUser(null);
        for (Users users : userService.findUsersByAbsence(absence2)){
            users.setAbsences(null);
            userService.saveCurrentUser();
        }
        absenceRepository.save(absence2);
        absenceRepository.deleteAbsence(absence2.getAbsenceId());
    }

}
