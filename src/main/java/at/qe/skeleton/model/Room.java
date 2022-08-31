package at.qe.skeleton.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "room")
public class Room implements Persistable<String>, Serializable, Comparable<Room>{

    @Id
    @SequenceGenerator(
            name = "room_sequence",
            sequenceName = "room_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "room_sequence"

    )
    private int roomId;
    private String roomName;
    private int floorNumber;
    private String roomNumber;
    private int numberOfSeats;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    /*Room has many sensors.*/

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL,fetch = FetchType.EAGER )
    private Set<Sensor> sensors;

    /*Employees work in Rooms*/
    @OneToMany(mappedBy = "office",cascade = CascadeType.ALL,fetch = FetchType.EAGER )
    private Set<Users> employees;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;


    public Department getDepartment() {
        return department;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public int getRoomId(){
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }

    public Set<Users> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Users> employees) {
        this.employees = employees;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.roomId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Room)) {
            return false;
        }
        final Room other = (Room) obj;
        return Objects.equals(this.roomId, other.roomId)
                && Objects.equals(this.roomName,other.roomName)
                && Objects.equals(this.roomNumber,other.roomNumber)
                && Objects.equals(this.roomType,other.roomType);
    }

    @Override
    public int compareTo(Room o) {
        return Integer.compare(roomId, o.getRoomId());
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
