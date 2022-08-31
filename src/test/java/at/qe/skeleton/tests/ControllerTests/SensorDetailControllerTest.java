package at.qe.skeleton.tests.ControllerTests;

import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.AuditLogService;
import at.qe.skeleton.services.DepartmentService;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.SensorService;
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
public class SensorDetailControllerTest {

    public JsfMock jsfMock = new JsfMock();

    @Autowired
    SensorService sensorService;

    @Autowired
    AuditLogService auditLogService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    RoomService roomService;

    @Autowired
    ApplicationContext applicationContext;

    SensorDetailController controller = new SensorDetailController();


    @BeforeEach
    public void init() {
        jsfMock.init(applicationContext);
        controller.setServices(sensorService,auditLogService,roomService,departmentService);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testSaveSensor(){
        Sensor sensor = sensorService.getAllSensors().stream().findFirst().get();
        double limitBefore = sensor.getGasLimit();
        int auditSize = auditLogService.getAllAuditLogs().size();
        controller.setSensor(sensor);

        controller.getSensor().setGasLimit(1999);
        controller.doSaveSensor();

        Assertions.assertNotEquals(limitBefore,sensor.getGasLimit());
        Assertions.assertEquals(auditSize+1, auditLogService.getAllAuditLogs().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testRemoveSensor(){
        Sensor sensor = sensorService.getAllSensors().stream().findFirst().get();
        int auditSize = auditLogService.getAllAuditLogs().size();
        controller.setSensor(sensor);
        controller.doRemoveSensor();

        Assertions.assertNull(controller.getSensor());
        for(Sensor sensor1 : sensorService.getAllSensors()){
            Assertions.assertNotEquals(sensor,sensor1);
        }
        Assertions.assertEquals(auditSize+1, auditLogService.getAllAuditLogs().size());
    }

}
