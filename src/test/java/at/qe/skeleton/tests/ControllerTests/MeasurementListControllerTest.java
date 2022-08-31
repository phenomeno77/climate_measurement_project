package at.qe.skeleton.tests.ControllerTests;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import at.qe.skeleton.ui.controllers.MeasurementListController;
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

import java.util.Collection;
import java.util.List;
import java.util.Map;

@SpringBootTest
@WebAppConfiguration
public class MeasurementListControllerTest {

    @Autowired
     MeasurementService measurementService;
    @Autowired
     SessionInfoBean sessionInfoBean;
    @Autowired
     UserService userService;
    @Autowired
    SensorService sensorService;

    public JsfMock jsfMock = new JsfMock();
    @Autowired
    ApplicationContext applicationContext;

    MeasurementListController controller = new MeasurementListController();

    @BeforeEach
    public void init() {
        jsfMock.init(applicationContext);
        controller.setServices(userService,sessionInfoBean,measurementService);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoGetAllMeasurements() {
        Collection<Measurement> doGetAllMeasurementsDaily = controller.doGetAllMeasurements(ReportFrequency.DAILY);
        Assertions.assertEquals(doGetAllMeasurementsDaily.size(),measurementService.getAllMeasurements(ReportFrequency.DAILY).size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetMeasurementById() {
       Measurement measurement = controller.getMeasurementById(22);
        Sensor sensor = sensorService.loadSensor("20");
        Assertions.assertEquals(sensor.getSensorId(), measurement.getSensor().getSensorId());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoDeleteMeasurement() {

        Measurement measurement = controller.getMeasurementById(22);
        int allMeasurements = measurementService.getAllMeasurements().size();

        controller.doDeleteMeasurement(measurement);
        Assertions.assertEquals(allMeasurements-1, measurementService.getAllMeasurements().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadAllMeasurementsBySensor() {
        Sensor sensor = sensorService.loadSensor("20");
        int allMeasurementsBySensor = measurementService.findMeasurementsBySensor(sensor).size();

        Assertions.assertEquals(allMeasurementsBySensor,controller.loadAllMeasurementsBySensor(sensor).size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadAllMeasurementsAllRooms() {
        int allMeasurementsBySensor = measurementService.getAllMeasurementsAllRooms(ReportFrequency.WEEKLY).size();

        Assertions.assertEquals(allMeasurementsBySensor,controller.doGetAllMeasurementsAllRooms(ReportFrequency.WEEKLY).size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadAllMeasurementsAllRoomsByUser() {
        int allMeasurementsBySensor = measurementService.getEmployeeRoomsReport(ReportFrequency.WEEKLY).size();

        Assertions.assertEquals(allMeasurementsBySensor,controller.doGetEmployeeReport(ReportFrequency.WEEKLY).size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadAllMeasurementsAllRoomsByDepartment() {
        Department department = userService.getAuthenticatedUser().getDepartment();
        int allMeasurementsBySensor = measurementService.getAllMeasurementsByDepartment(department,ReportFrequency.WEEKLY).size();

        Assertions.assertEquals(allMeasurementsBySensor,controller.doGetAllMeasurementsAllRoomsByDepartment(ReportFrequency.WEEKLY).size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadAllMeasurementsAllByDepartment() {
        Department department = userService.getAuthenticatedUser().getDepartment();

        Assertions.assertEquals(0,controller.doGetAllMeasurementsByDepartment(ReportFrequency.WEEKLY).size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadAllMeasurementsAllByUser() {
        Department department = userService.getAuthenticatedUser().getDepartment();

        Assertions.assertEquals(0,controller.doGetUsersReport(ReportFrequency.WEEKLY).size());
    }


}
