package at.qe.skeleton.tests.ServiceTests;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.DepartmentService;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

/**
 * Tests for {@link RoomService}.
 */
@SpringBootTest
@WebAppConfiguration
class RoomServiceTest {
    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    @Autowired
    DepartmentService departmentService;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllRooms() {
        int allRooms = roomService.getAllRooms().size();
        Assertions.assertEquals(allRooms,roomService.getAllRooms().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadRoomByID() {
        Room room = roomService.loadRoom(10);
        Assertions.assertNotNull(room);
        Assertions.assertEquals("IT.OFFICE A",room.getRoomName());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadRoomByRoomName() {
        Room room = roomService.loadRoomByName("IT.OFFICE A");
        Assertions.assertNotNull(room);
        Assertions.assertEquals("IT.OFFICE A",room.getRoomName());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testSaveRoom() {
        Room room = new Room();
        room.setRoomId(100);
        room.setRoomType(RoomType.OFFICE);
        room.setRoomName("TestRoom");
        roomService.saveRoom(room);

        Assertions.assertNotNull(room);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetUsersByRoom() {
        Room room = roomService.loadRoomByName("IT.OFFICE A");

        Collection<Users> usersByRoom = new HashSet<>();
        usersByRoom.add(userService.loadUser("user1"));
        usersByRoom.add(userService.loadUser("user2"));
        usersByRoom.add(userService.loadUser("user3"));
        usersByRoom.add(userService.loadUser("user4"));
        usersByRoom.add(userService.loadUser("user5"));
        usersByRoom.add(userService.loadUser("hulk"));

        Assertions.assertEquals(usersByRoom.size(), roomService.getUsersByRoom(room).size());
        Assertions.assertTrue(roomService.getUsersByRoom(room).containsAll(usersByRoom));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetPresentUsersByRoom() {
        Room room = roomService.loadRoomByName("IT.OFFICE A");
        int presentUsers = roomService.getPresentUsersByOffice(room).size();
        Users users = userService.loadUser("user1");
        users.setUserStatus(UserStatus.ABSENT);
        userService.saveUser(users);

        Assertions.assertEquals(presentUsers-1,roomService.getPresentUsersByOffice(room).size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "spiderman", authorities = {"FACILITY_MANAGER"})
    void testDeleteRoom() {
         Room room = roomService.loadRoomByName("IT.OFFICE A");
         room = roomService.loadRoom(room.getRoomId());
         int rooms = roomService.getAllRooms().size();
        roomService.deleteRoom(room);

        Assertions.assertEquals(rooms-1,roomService.getAllRooms().size()); //10
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllPublicRooms(){
        List<Room> publicRooms = new ArrayList<>();
        for(Room room : roomService.getAllRooms()){
            if(room.getRoomType() != RoomType.OFFICE){
                publicRooms.add(room);
            }
        }


        Assertions.assertEquals(publicRooms.size(),roomService.getAllPublicRooms().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllOffices(){
        List<Room> offices = new ArrayList<>();
        for(Room room : roomService.getAllRooms()){
            if(room.getRoomType() == RoomType.OFFICE){
                offices.add(room);
            }
        }


        Assertions.assertEquals(offices.size(),roomService.getAllOffices().size());
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetPublicRoomsPerDepartment(){
        Department department1 = departmentService.loadDepartment(2);
        int allPublicRooms=0;

        for(Room room1 : roomService.getAllRooms()){
            if(!room1.getRoomType().equals(RoomType.OFFICE)){
                allPublicRooms++;
            }
        }

        Assertions.assertEquals(allPublicRooms,roomService.getPublicRoomsPerDepartment(department1).size());

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "hulk", authorities = {"MANAGER"})
    void testGetRoomsPerDepartmentManager(){

        Users departmentManager = userService.getAuthenticatedUser();

        int roomsByDepartment=0;
        for(Room room : roomService.getAllRooms()){
            if(departmentManager.getDepartment().equals(room.getDepartment())){
                roomsByDepartment++;
            }
        }

        Assertions.assertEquals(roomsByDepartment,roomService.getRoomsByDepartmentManager(departmentManager).size());

    }


}
