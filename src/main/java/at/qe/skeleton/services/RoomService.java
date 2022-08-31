package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.RoomRepository;
import at.qe.skeleton.repositories.SensorRepository;
import at.qe.skeleton.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Service for accessing and manipulating rooms.
 */
@Transactional
@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SensorRepository sensorRepository;

    /**
     * Returns a collection of all rooms.
     *
     * @return all stored rooms
     */

    public Collection<Room> getAllRooms() {
        return roomRepository.findAll();
    }


    public Collection<Room> getAllPublicRooms() {
        return roomRepository.findAll().stream()
                .filter(room -> room.getRoomType() != RoomType.OFFICE)
                .collect(Collectors.toList());
    }

    public Collection<Room> getAllOffices() {
        return roomRepository.findAll().stream()
                .filter(room -> room.getRoomType() == RoomType.OFFICE)
                .collect(Collectors.toList());
    }

    public Collection<Room> getRoomsByDepartmentManager(Users departmentManager) {
        return roomRepository.findAll().stream()
                .filter(room -> departmentManager.getDepartment().equals(room.getDepartment()))
                .collect(Collectors.toList());
    }

    /**
     * Loads a single room identified by its id.
     *
     * @param id the id of the given room to search for
     * @return the room with the given id
     */
    public Room loadRoom(int id) {
        return roomRepository.findById(id).orElse(null);
    }

    public Room loadRoomByName(String roomName) {
        return roomRepository.findByRoomName(roomName);
    }

    /**
     * Saves the room.
     *
     * @param room the room to save
     * @return the updated/saved room
     */
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    /**
     * Deletes the room.
     *
     * @param room the room to delete
     */
    @Transactional
    public void deleteRoom(Room room) {
        Room room2 = roomRepository.findByRoomName(room.getRoomName());

        if (!userService.findUsersByOffice(room).isEmpty()) {
            for (Users users : userService.findUsersByOffice(room)) {
                users.setOffice(null);
                userService.updateUser(users);
            }
            room.getEmployees().removeAll(room2.getEmployees());
            roomRepository.save(room);
        }

        if (!room.getSensors().isEmpty()) {
            for (Sensor sensor : room.getSensors()) {
                sensor.setRoom(null);
                sensorRepository.save(sensor);
            }
            room.getSensors().removeAll(room2.getSensors());
            roomRepository.save(room);
        }

        sensorRepository.findAll();

        roomRepository.deleteById(room2.getRoomId());
    }

    /**
     * Return users by room.
     *
     * @param room
     * @return
     */
    public Collection<Users> getUsersByRoom(Room room) {
        return userRepository.findAll().stream()
                .filter(user -> room.equals(user.getOffice()))
                .collect(Collectors.toList());
    }

    /**
     * Get present users by room
     *
     * @param office
     * @return
     */
    public Collection<Users> getPresentUsersByOffice(Room office) {
        return userRepository.findAll().stream()
                .filter(user -> office.equals(user.getOffice()) && user.getUserStatus() == UserStatus.PRESENT)
                .collect(Collectors.toList());
    }


    public Collection<Room> getPublicRoomsPerDepartment(Department department) {
        Collection<Room> publicRoomsInDepartment = new HashSet<>();
        Collection<Room> rooms = getAllRooms();
        for (Room room : rooms) {
            if (room.getRoomType() != RoomType.OFFICE && room.getDepartment().getDepartmentId() == department.getDepartmentId()) {
                publicRoomsInDepartment.add(room);
            }
        }
        return publicRoomsInDepartment;
    }
}
