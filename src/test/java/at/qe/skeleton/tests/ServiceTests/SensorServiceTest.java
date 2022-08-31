package at.qe.skeleton.tests.ServiceTests;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Tests for {@link SensorService}.
 */
@SpringBootTest
@WebAppConfiguration
class SensorServiceTest {
    @Autowired
    SensorService sensorService;

    @Autowired
    UserService userService;

    @Autowired
    RoomService roomService;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllSensors(){
        int allSensors = sensorService.getAllSensors().size();
        Assertions.assertEquals(allSensors,sensorService.getAllSensors().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadSensorByID(){
        Sensor sensor = sensorService.loadSensor("20");
        Assertions.assertNotNull(sensor);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testSaveSensor(){

        Sensor sensor = sensorService.loadSensor("20");
        double oldLimit = sensor.getGasLimit();

        Assertions.assertEquals(oldLimit,sensor.getGasLimit());

        sensor.setGasLimit(234.0);
        sensorService.saveSensor(sensor);

        Assertions.assertNotEquals(oldLimit,sensor.getGasLimit());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testAddNewSensor(){
        Room room = roomService.loadRoom(10);
        Sensor sensor = new Sensor();
        sensor.setSensorId("test_sensor");
        sensor.setRoom(room);
        sensorService.saveSensor(sensor);

        Assertions.assertEquals(room, sensorService.loadSensor("test_sensor").getRoom());

        Room room2 = roomService.loadRoom(20);
        Sensor sensor2 = new Sensor();
        sensor.setSensorId("test_sensor");
        sensor.setRoom(room2);
        sensorService.saveSensor(sensor);

        Assertions.assertEquals(room2, sensorService.loadSensor("test_sensor").getRoom());



        Assertions.assertTrue(sensorService.getAllSensors().contains(sensor));
        Assertions.assertEquals("test_sensor", sensorService.loadSensor("test_sensor").getSensorId());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetSensorsByRoom(){
        Room room = roomService.loadRoom(20);
        Assertions.assertEquals(1,sensorService.getAllSensorsByRoom(room).size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "spiderman", authorities = {"FACILITY_MANAGER"})
    void testDeleteSensor(){

        Sensor sensorToBeDeleted = sensorService.loadSensor("20");
        double oldLimit = sensorToBeDeleted.getGasLimit();
        int allSensors = sensorService.getAllSensors().size();

        Assertions.assertEquals(oldLimit,sensorToBeDeleted.getGasLimit());

        sensorToBeDeleted.setGasLimit(234.0);
        sensorService.saveSensor(sensorToBeDeleted);

        Assertions.assertNotEquals(oldLimit,sensorToBeDeleted.getGasLimit());

        sensorService.deleteSensor(sensorToBeDeleted);

        Assertions.assertNull(sensorService.loadSensor(sensorToBeDeleted.getSensorId()));

        Assertions.assertEquals(sensorService.getAllSensors().size(),allSensors-1);

        for(Sensor sensor1 : sensorService.getAllSensors()){
            Assertions.assertNotEquals(sensor1,sensorToBeDeleted);
        }
    }


}
