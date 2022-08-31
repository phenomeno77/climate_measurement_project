package at.qe.skeleton.tests.ControllerTests;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.AuditLogService;
import at.qe.skeleton.services.DepartmentService;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.ui.controllers.SensorAddController;
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
public class SensorAddControllerTest {

    public JsfMock jsfMock = new JsfMock();

    @Autowired
    SensorService sensorService;

    @Autowired
    AuditLogService auditLogService;

    @Autowired
    RoomService roomService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    ApplicationContext applicationContext;

    SensorAddController controller = new SensorAddController();


    @BeforeEach
    public void init() {
        jsfMock.init(applicationContext);
        controller.setServices(sensorService,auditLogService,roomService,departmentService);
    }



    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoSaveSensor(){
       Room room = roomService.getAllRooms().stream().findFirst().orElse(null);
       Sensor sensor = new Sensor();

       controller.setSensor(sensor);
       controller.getSensor().setSensorId("999");
       controller.getSensor().setRoom(room);
       controller.getSensor().setGasLimit(20.0);
       controller.doSaveSensor();

       Assertions.assertEquals(1,sensorService.getAllSensorsByRoom(room).size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoUpdateSensor(){
        Room room = roomService.getAllRooms().stream().findFirst().orElse(null);
        Sensor sensor = new Sensor();

        controller.setSensor(sensor);
        controller.getSensor().setSensorId("999");
        controller.getSensor().setRoom(room);
        controller.getSensor().setGasLimit(20.0);
        controller.getSensor().setTemperatureLimitLow(0.0);
        controller.getSensor().setTemperatureLimitHigh(30.0);
        controller.getSensor().setHumidityLimitLow(0.0);
        controller.getSensor().setHumidityLimitHigh(30.0);
        controller.getSensor().setLuxLimit(2.0);
        controller.getSensor().setDecibelLimit(30.0);
        controller.getSensor().setPressureLimitLow(0.0);
        controller.getSensor().setPressureLimitHigh(30.0);
        controller.doSaveSensor();

        Assertions.assertEquals(1,sensorService.getAllSensorsByRoom(room).size());
        Assertions.assertEquals(20.0,controller.getSensor().getGasLimit());
        Assertions.assertEquals(0.0,controller.getSensor().getTemperatureLimitLow());
        Assertions.assertEquals(30.0,controller.getSensor().getTemperatureLimitHigh());
        Assertions.assertEquals(0.0,controller.getSensor().getHumidityLimitLow());
        Assertions.assertEquals(30.0,controller.getSensor().getHumidityLimitHigh());
        Assertions.assertEquals(2.0,controller.getSensor().getLuxLimit());
        Assertions.assertEquals(30.0,controller.getSensor().getDecibelLimit());
        Assertions.assertEquals(0.0,controller.getSensor().getPressureLimitLow());
        Assertions.assertEquals(30.0,controller.getSensor().getPressureLimitHigh());

    }



}
