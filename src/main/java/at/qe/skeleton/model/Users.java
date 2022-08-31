package at.qe.skeleton.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * Entity representing users.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Entity
@Table(name = "users")
public class Users implements Persistable<String>, Serializable, Comparable<Users> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 100)
    private String username;


    @ManyToOne(optional = true)
    private Users createUsers;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @ManyToOne(optional = true)
    private Users updateUsers;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    private String password;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDateTime nextEmailPost;

    @Enumerated(EnumType.STRING)
    private MailInterval mailSettings = MailInterval.OFF;


    boolean enabled;

    @JoinColumn(name = "user_roles", nullable = true)
    @Enumerated(EnumType.STRING)
    private UserRole roles;

    /**
    * nullable = true only for testing purposes.
    * */
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    private Room office;

    @OneToOne(mappedBy = "departmentManager")
    private Department managedDepartment;

    /**
     * nullable = true only for testing purposes.
     * */
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL,fetch = FetchType.EAGER )
    private Set<Absence> absences;

    public LocalDateTime getNextEmailPost() {
        return nextEmailPost;
    }

    public void setNextEmailPost(LocalDateTime nextEmailPost) {
        this.nextEmailPost = nextEmailPost;
    }

    public Room getOffice() {
        return office;
    }

    public void setOffice(Room office) {
        this.office = office;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    /**Default value*/
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.PRESENT;

    public MailInterval getMailSettings() {
        return mailSettings;
    }

    public void setMailSettings(MailInterval mailSettings) {
        this.mailSettings = mailSettings;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public UserRole getRoles() {
        return roles;
    }

    public void setRoles(UserRole roles) {
        this.roles = roles;
    }

    public Users getCreateUser() {
        return createUsers;
    }

    public void setCreateUser(Users createUsers) {
        this.createUsers = createUsers;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Users getUpdateUser() {
        return updateUsers;
    }

    public void setUpdateUser(Users updateUsers) {
        this.updateUsers = updateUsers;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Users)) {
            return false;
        }
        final Users other = (Users) obj;
        return Objects.equals(this.username, other.username);
    }

    @Override
    public String toString() {
        return "at.qe.skeleton.model.User[ id=" + username + " ]";
    }

    @Override
    public String getId() {
        return getUsername();
    }

    public void setId(String id) {
        setUsername(id);
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

	@Override
	public int compareTo(Users o) {
		return this.username.compareTo(o.getUsername());
	}


    public Set<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(Set<Absence> absences) {
        this.absences = absences;
    }

    public Department getManagedDepartment() {
        return managedDepartment;
    }

    public void setManagedDepartment(Department managedDepartment) {
        this.managedDepartment = managedDepartment;
    }
}
