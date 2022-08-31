package at.qe.skeleton.tests.ControllerTests;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.model.UserStatus;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.controllers.RoomListController;
import org.joinfaces.test.mock.JsfMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;


@SpringBootTest
@WebAppConfiguration
class RoomListControllerTest {

    public JsfMock jsfMock = new JsfMock();


    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    @Autowired
    ApplicationContext applicationContext;

    RoomListController controller = new RoomListController();

    @BeforeEach
    public void init() {
        jsfMock.init(applicationContext);
        controller.setServices(roomService);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllRooms() {
        int allRooms = roomService.getAllRooms().size();
        Assertions.assertEquals(allRooms,controller.getRooms().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetUsersByRoom() {
        Room room = roomService.loadRoomByName("IT.OFFICE A");
        Assertions.assertEquals(6,controller.getUsersByRoom(room).size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetPresentUsersByRoom() {
        Room room = roomService.loadRoomByName("IT.OFFICE A");
        Users users = userService.loadUser("user1");
        users.setUserStatus(UserStatus.ABSENT);
        userService.saveUser(users);

        Assertions.assertEquals(5,controller.getPresentUsersByRoom(room).size());
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

        Assertions.assertEquals(roomsByDepartment,controller.getRoomsByDepartmentManager(departmentManager).size());

    }

}
