package at.qe.skeleton.tests.ControllerTests;

import at.qe.skeleton.model.Department;
import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.AuditLogService;
import at.qe.skeleton.services.DepartmentService;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.ui.controllers.RoomDetailController;
import at.qe.skeleton.ui.controllers.SensorDetailController;
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
public class RoomDetailControllerTest {
    public JsfMock jsfMock = new JsfMock();


    @Autowired
    AuditLogService auditLogService;

    @Autowired
    RoomService roomService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    ApplicationContext applicationContext;

    RoomDetailController controller = new RoomDetailController();


    @BeforeEach
    public void init() {
        jsfMock.init(applicationContext);
        controller.setServices(roomService,auditLogService,departmentService);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testSaveRoom(){
       Room roomToLoad = roomService.loadRoomByName("IT.OFFICE A");
       String newRoomName = "New Room Name";
       controller.setRoom(roomToLoad);

       Assertions.assertEquals("IT.OFFICE A", controller.getRoom().getRoomName());

       controller.getRoom().setRoomName(newRoomName);
       controller.doSaveRoom();

        Assertions.assertEquals(newRoomName, controller.getRoom().getRoomName());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testRemoveRoom(){
        Room roomToLoad = roomService.loadRoomByName("IT.OFFICE A");
        int allRooms = roomService.getAllRooms().size();

        Assertions.assertEquals(allRooms, roomService.getAllRooms().size());

        controller.setRoom(roomToLoad);
        controller.doRemoveRoom();


        Assertions.assertEquals(allRooms-1, roomService.getAllRooms().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllDepartments(){
        int allDepartments = departmentService.getAllDepartments().size();

        Assertions.assertEquals(allDepartments,controller.getDepartments().size());
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testChangeDepartment(){
        Room roomToLoad = roomService.loadRoomByName("IT.OFFICE A");

        Assertions.assertEquals("IT",roomToLoad.getDepartment().getDepartmentName());

        controller.setRoom(roomToLoad);
        controller.setDepartment("Biology");
        controller.doSaveRoom();

        Assertions.assertEquals("Biology",roomToLoad.getDepartment().getDepartmentName());
    }


}
