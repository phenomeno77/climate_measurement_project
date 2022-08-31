package at.qe.skeleton.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "absence")
public class Absence implements Persistable<String>, Serializable,Comparable<Absence> {

    @Id
    @SequenceGenerator(
            name = "absence_sequence",
            sequenceName = "absence_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "absence_sequence"

    )
    private int absenceId;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private AbsenceReason absenceReason;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private Users users;

    public int getAbsenceId() {
        return absenceId;
    }

    public void setAbsenceId(int absenceId) {
        this.absenceId = absenceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public AbsenceReason getAbsenceReason() {
        return absenceReason;
    }

    public void setAbsenceReason(AbsenceReason absenceReason) {
        this.absenceReason = absenceReason;
    }

    public Users getUser() {
        return users;
    }

    public void setUser(Users users) {
        this.users = users;
    }



    @Override
    public String getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public int compareTo(@NotNull Absence o) {
        return Integer.compare(absenceId, o.getAbsenceId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Absence)) {
            return false;
        }
        final Absence other = (Absence) obj;
        return Objects.equals(this.absenceId,other.absenceId)
                && Objects.equals(this.absenceReason,other.absenceReason)
                && Objects.equals(this.title,other.title)
                && Objects.equals(this.startDate,other.startDate)
                && Objects.equals(this.endDate,other.endDate);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.absenceId);
        return hash;
    }


}
