package at.qe.skeleton.tests;

import at.qe.skeleton.model.MailInterval;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.beans.MeasurementDataInitializer;
import org.joinfaces.test.mock.JsfMock;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.Date;

@SpringBootTest
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
 class MeasurementDataInitializerTest {

    @Autowired
    MeasurementService measurementService;

    @Autowired
    UserService userService;

    @Autowired
    RoomService roomService;

    @Autowired
    SensorService sensorService;

    public JsfMock jsfMock = new JsfMock();

    @Autowired
    ApplicationContext applicationContext;

    MeasurementDataInitializer controller = new MeasurementDataInitializer();

    @BeforeEach
    public void init() {
        jsfMock.init(applicationContext);
        controller.setServices(measurementService,userService,roomService);
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testSelectIntervalOneDay() throws IOException {
        for(Measurement measurement : measurementService.getAllMeasurements()){
            measurementService.deleteMeasurement(measurement);
        }
        Assertions.assertEquals(0,measurementService.getAllMeasurements().size(),"No measurements at all");

        //24 hours * 6 types of measurements
        int measurementsOneDay = 24 * 6;
        controller.selectDayInterval(1);
        Assertions.assertEquals(measurementsOneDay,measurementService.getAllMeasurements().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testInitializeMeasurementDataForIntervalOneDay() {
        for(Measurement measurement : measurementService.getAllMeasurements()){
            measurementService.deleteMeasurement(measurement);
        }
        Assertions.assertEquals(0,measurementService.getAllMeasurements().size(),"No measurements at all");

        //24 hours * 6 types of measurements
        int measurementsOneDay = 24 * 6;
        controller.initializeMeasurementDataForInterval(1);
        Assertions.assertEquals(measurementsOneDay,measurementService.getAllMeasurements().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testInitializeMeasurementBySensorOneDay() {
        for(Measurement measurement : measurementService.getAllMeasurements()){
            measurementService.deleteMeasurement(measurement);
        }
        Assertions.assertEquals(0,measurementService.getAllMeasurements().size(),"No measurements at all");

        //24 hours * 6 types of measurements
        int measurementsOneDay = 24 * 6;
        Sensor sensor = sensorService.loadSensor("20");
        controller.initializeMeasurementBySensor(1, sensor);
        Assertions.assertEquals(measurementsOneDay,measurementService.getAllMeasurements().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testCreateTemp() {
        for(Measurement measurement : measurementService.getAllMeasurements()){
            measurementService.deleteMeasurement(measurement);
        }
        Assertions.assertEquals(0,measurementService.getAllMeasurements().size(),"No measurements at all");
        Date date = new Date();

        Sensor sensor = sensorService.loadSensor("20");
        controller.createTemp(25.0, date,sensor);
        Assertions.assertEquals(1,measurementService.getAllMeasurements().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testCreateGas() {
        for(Measurement measurement : measurementService.getAllMeasurements()){
            measurementService.deleteMeasurement(measurement);
        }
        Assertions.assertEquals(0,measurementService.getAllMeasurements().size(),"No measurements at all");
        Date date = new Date();

        Sensor sensor = sensorService.loadSensor("20");
        controller.createGas(25.0, date,sensor);
        Assertions.assertEquals(1,measurementService.getAllMeasurements().size());
    }
    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testCreateLux() {
        for(Measurement measurement : measurementService.getAllMeasurements()){
            measurementService.deleteMeasurement(measurement);
        }
        Assertions.assertEquals(0,measurementService.getAllMeasurements().size(),"No measurements at all");
        Date date = new Date();

        Sensor sensor = sensorService.loadSensor("20");
        controller.createLux( date,sensor);
        Assertions.assertEquals(1,measurementService.getAllMeasurements().size());
    }
    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testCreatePressure() {
        for(Measurement measurement : measurementService.getAllMeasurements()){
            measurementService.deleteMeasurement(measurement);
        }
        Assertions.assertEquals(0,measurementService.getAllMeasurements().size(),"No measurements at all");
        Date date = new Date();

        Sensor sensor = sensorService.loadSensor("20");
        controller.createPressure( date,sensor);
        Assertions.assertEquals(1,measurementService.getAllMeasurements().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testCreateHumidity() {
        for(Measurement measurement : measurementService.getAllMeasurements()){
            measurementService.deleteMeasurement(measurement);
        }
        Assertions.assertEquals(0,measurementService.getAllMeasurements().size(),"No measurements at all");
        Date date = new Date();

        Sensor sensor = sensorService.loadSensor("20");
        controller.createHumidity(25.0, date,sensor);
        Assertions.assertEquals(1,measurementService.getAllMeasurements().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testCreateDecibel() {
        for(Measurement measurement : measurementService.getAllMeasurements()){
            measurementService.deleteMeasurement(measurement);
        }
        Assertions.assertEquals(0,measurementService.getAllMeasurements().size(),"No measurements at all");
        Date date = new Date();

        Sensor sensor = sensorService.loadSensor("20");
        controller.createDecibel(25.0, date,sensor);
        Assertions.assertEquals(1,measurementService.getAllMeasurements().size());
    }


}
