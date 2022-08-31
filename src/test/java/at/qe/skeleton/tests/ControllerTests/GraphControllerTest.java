package at.qe.skeleton.tests.ControllerTests;

import at.qe.skeleton.model.Department;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.ReportFrequency;
import at.qe.skeleton.model.Room;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.controllers.GraphController;
import org.joinfaces.test.mock.JsfMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.primefaces.model.charts.line.LineChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Map;

@SpringBootTest
@WebAppConfiguration
public class GraphControllerTest {

    @Autowired
    MeasurementService measurementService;

    @Autowired
    UserService userService;



    public JsfMock jsfMock = new JsfMock();
    @Autowired
    ApplicationContext applicationContext;

    GraphController controller = new GraphController();

    @BeforeEach
    public void init() {
        jsfMock.init(applicationContext);
        controller.setServices(userService,measurementService);
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testCreateGraphAllRooms() {
        Map<Room, List<Measurement>> measurementMap = measurementService.getEmployeeRoomsReport(ReportFrequency.WEEKLY);

        Assertions.assertEquals(measurementMap.values().size(),controller.createGraphUserRoomsByInterval(ReportFrequency.WEEKLY).size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testCreateGraphAllRoomsByDepartment() {
        Department department = userService.getAuthenticatedUser().getDepartment();
        Map<Room, List<Measurement>> measurementMap = measurementService.getAllMeasurementsByDepartment(department,ReportFrequency.WEEKLY);
        Assertions.assertEquals(measurementMap.values().size(),controller.createGraphAllRoomsByDepartment(ReportFrequency.WEEKLY).size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testCreateGraphAllUserRooms() {
        Map<Room, List<Measurement>> measurementMap = measurementService.getAllMeasurementsAllRooms(ReportFrequency.WEEKLY);
        Assertions.assertEquals(measurementMap.values().size(),controller.createGraphAllRooms(ReportFrequency.WEEKLY).size());
        }

}
