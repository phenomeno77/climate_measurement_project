package at.qe.skeleton.tests.ControllerTests;

import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.ui.controllers.SensorListController;
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
class SensorListControllerTest {

    public JsfMock jsfMock = new JsfMock();

    @Autowired
    SensorService sensorService;

    @Autowired
    RoomService roomService;

    @Autowired
    ApplicationContext applicationContext;

    SensorListController controller = new SensorListController();


    @BeforeEach
    public void init() {
        jsfMock.init(applicationContext);
        controller.setServices(sensorService);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllSensors(){
        int allSensors = sensorService.getAllSensors().size();
        Assertions.assertEquals(allSensors,controller.getSensors().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllSensorsByRoom(){
        int allSensors = sensorService.getAllSensorsByRoom(roomService.loadRoom(10)).size();
        Assertions.assertEquals(allSensors,controller.getSensorsByRoom(roomService.loadRoom(10)).size());
    }




}

