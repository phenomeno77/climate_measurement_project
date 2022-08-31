package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Scope("view")
public class RoomListController {

    @Autowired
    RoomService roomService;

    /**Set Services for the JUnit tests
     *
     * @param roomService
     */
    public void setServices(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Get all rooms
     * @return
     */
    public Collection<Room> getRooms(){
        return roomService.getAllRooms();
    }

    /**Get all users by room
     *
     * @param room
     * @return
     */
    public Collection<Users> getUsersByRoom(Room room){
      return roomService.getUsersByRoom(room);
    }

    public Integer getNumberUsersByRoom(Room room){
        return roomService.getUsersByRoom(room).size();
    }

    /**Get all present users per room
     *
     * @param office
     * @return
     */
    public Collection<Users> getPresentUsersByRoom(Room office){
        return roomService.getPresentUsersByOffice(office);
    }

    public Integer getNumberPresentUsersByRoom(Room office){
        return roomService.getPresentUsersByOffice(office).size();
    }


    /**Get all rooms by department
     *
     * @param departmentManager
     * @return
     */
    public Collection<Room> getRoomsByDepartmentManager(Users departmentManager){
        return roomService.getRoomsByDepartmentManager(departmentManager);
    }

}
