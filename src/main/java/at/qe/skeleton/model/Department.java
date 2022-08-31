package at.qe.skeleton.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "department")
public class Department implements Persistable<String>, Serializable, Comparable<Department>{
    @Id
    @SequenceGenerator(
            name = "department_sequence",
            sequenceName = "department_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "department_sequence"

    )

    private int departmentId;


    private String departmentName;

    @OneToMany(mappedBy = "department")
    private Set<Users> users; //evtl. change to different name than users (Office same name)

    @OneToMany(mappedBy = "department",fetch = FetchType.EAGER)
    private Set<Room> rooms;

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    @OneToOne
    @JoinColumn(name = "departmentManager")
    private Users departmentManager;

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Set<Users> getUsers() {
        return users;
    }

    public void setUsers(Set<Users> users) {
        this.users = users;
    }

    public Users getDepartmentManager() {
        return departmentManager;
    }

    public void setDepartmentManager(Users departmentManager) {
        this.departmentManager = departmentManager;
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.departmentId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Department)) {
            return false;
        }
        final Department other = (Department) obj;
        return Objects.equals(this.departmentId, other.departmentId)
                && Objects.equals(this.departmentName,other.departmentName)
                && Objects.equals(this.departmentManager.getUsername(),other.departmentManager.getUsername());
    }

    @Override
    public int compareTo(Department o) {
        return Integer.compare(departmentId, o.getDepartmentId());
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}

