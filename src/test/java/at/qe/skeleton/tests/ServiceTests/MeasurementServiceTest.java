package at.qe.skeleton.tests.ServiceTests;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.Instant;
import java.util.*;

/**
 * Tests for {@link MeasurementService}.
 */
@SpringBootTest
@WebAppConfiguration
class MeasurementServiceTest {
    @Autowired
    MeasurementService measurementService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    RoomService roomService;
    @Autowired
    SensorService sensorService;
    @Autowired
    UserService userService;


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllMeasurements() {
        Department department = departmentService.loadDepartment(1);
        int daily = measurementService.getAllMeasurements(ReportFrequency.DAILY).size();
        int weekly = measurementService.getAllMeasurements( ReportFrequency.WEEKLY).size();
        int monthly = measurementService.getAllMeasurements(ReportFrequency.MONTHLY).size();
        Collection<Measurement> allMeasurementsDaily = measurementService.getAllMeasurements(ReportFrequency.DAILY);
        Collection<Measurement> allMeasurementsWeekly = measurementService.getAllMeasurements(ReportFrequency.WEEKLY);
        Collection<Measurement> allMeasurementsMonthly = measurementService.getAllMeasurements(ReportFrequency.MONTHLY);

        Assertions.assertEquals(daily, allMeasurementsDaily.size());
        Assertions.assertEquals(weekly, allMeasurementsWeekly.size());
        Assertions.assertEquals(monthly, allMeasurementsMonthly.size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllPublicRoomMeasurementsByDepartment() {
        Department department = departmentService.loadDepartment(1);
        int daily = measurementService.getAllPublicRoomMeasurementsByDepartment(department,ReportFrequency.DAILY).size();
        int weekly = measurementService.getAllPublicRoomMeasurementsByDepartment(department, ReportFrequency.WEEKLY).size();
        int monthly = measurementService.getAllPublicRoomMeasurementsByDepartment(department, ReportFrequency.MONTHLY).size();
        Map<Measurement, String> allMeasurementsDaily = measurementService.getAllPublicRoomMeasurementsByDepartment(department, ReportFrequency.DAILY);
        Map<Measurement, String> allMeasurementsWeekly = measurementService.getAllPublicRoomMeasurementsByDepartment(department, ReportFrequency.WEEKLY);
        Map<Measurement, String> allMeasurementsMonthly = measurementService.getAllPublicRoomMeasurementsByDepartment(department, ReportFrequency.MONTHLY);

        Assertions.assertEquals(daily, allMeasurementsDaily.size());
        Assertions.assertEquals(weekly, allMeasurementsWeekly.size());
        Assertions.assertEquals(monthly, allMeasurementsMonthly.size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllPublicRoomMeasurementsFrequency() {
        Department department = departmentService.loadDepartment(1);
        int daily = measurementService.getAllPublicRoomMeasurements(ReportFrequency.DAILY).size();
        int weekly = measurementService.getAllPublicRoomMeasurements( ReportFrequency.WEEKLY).size();
        int monthly = measurementService.getAllPublicRoomMeasurements(ReportFrequency.MONTHLY).size();
        Map<Measurement, String> allMeasurementsDaily = measurementService.getAllPublicRoomMeasurements( ReportFrequency.DAILY);
        Map<Measurement, String> allMeasurementsWeekly = measurementService.getAllPublicRoomMeasurements( ReportFrequency.WEEKLY);
        Map<Measurement, String> allMeasurementsMonthly = measurementService.getAllPublicRoomMeasurements( ReportFrequency.MONTHLY);

        Assertions.assertEquals(daily, allMeasurementsDaily.size());
        Assertions.assertEquals(weekly, allMeasurementsWeekly.size());
        Assertions.assertEquals(monthly, allMeasurementsMonthly.size());
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadMeasurement() {
        Measurement loadedMeasurement = measurementService.loadMeasurement(23);
        loadedMeasurement.setValue(500.0);

        measurementService.saveMeasurement(loadedMeasurement);

        Assertions.assertEquals(500, loadedMeasurement.getValue());
        Assertions.assertEquals(MeasurementType.TEMPERATURE, loadedMeasurement.getType());
        Assertions.assertEquals("20", loadedMeasurement.getSensor().getSensorId());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testSaveMeasurement() {
        Measurement measurementToSave = new Measurement();
        measurementToSave.setSensor(sensorService.loadSensor("23"));

        measurementToSave.setType(MeasurementType.LIGHT_INTENSITY);
        measurementToSave.setValue(12);

        Measurement savedMeasurement = measurementService.saveMeasurement(measurementToSave);
        Measurement loadedMeasurement = measurementService.loadMeasurement(savedMeasurement.getMeasurementId());

        Assertions.assertEquals(12, loadedMeasurement.getValue());
        Assertions.assertEquals(MeasurementType.LIGHT_INTENSITY, loadedMeasurement.getType());
        Assertions.assertEquals("23", loadedMeasurement.getSensor().getSensorId());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testFindMeasurementBySensor() {
        Sensor sensor = sensorService.loadSensor("22");
        Collection<Measurement> measurementsBySensor = measurementService.findMeasurementsBySensor(sensor);

        Assertions.assertEquals(2, measurementsBySensor.size());
        Assertions.assertEquals(21.4, Objects.requireNonNull(measurementsBySensor.stream().findFirst().orElse(null)).getValue());
        Assertions.assertEquals(MeasurementType.TEMPERATURE, Objects.requireNonNull(measurementsBySensor.stream().findFirst().orElse(null)).getType());
        Assertions.assertEquals("2022-04-01 10:40:33.0", Objects.requireNonNull(measurementsBySensor.stream().findFirst().orElse(null)).getTimestamp().toString());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user6", authorities = {"EMPLOYEE"})
    void testGetEmployeeOfficeReport() {
        Users user = userService.loadUser("user6");

        Measurement measurementToSave1 = new Measurement();
        measurementToSave1.setSensor(sensorService.loadSensor("22"));
        measurementToSave1.setTimestamp(Date.from(Instant.parse("2022-04-22T00:00:00.000Z")));
        measurementToSave1.setType(MeasurementType.LIGHT_INTENSITY);
        measurementToSave1.setValue(12);
        measurementService.saveMeasurement(measurementToSave1);

        Measurement measurementToSave2 = new Measurement();
        measurementToSave2.setSensor(sensorService.loadSensor("22"));
        measurementToSave2.setTimestamp(Date.from(Instant.parse("2022-04-19T00:00:00.000Z")));
        measurementToSave2.setType(MeasurementType.LIGHT_INTENSITY);
        measurementToSave2.setValue(12);
        measurementService.saveMeasurement(measurementToSave2);


        Collection<Measurement> userOfficeReportDaily = measurementService.getEmployeeOfficeReport(ReportFrequency.DAILY);
        Collection<Measurement> userOfficeReportWeekly = measurementService.getEmployeeOfficeReport(ReportFrequency.WEEKLY);
        Collection<Measurement> userOfficeReportMonthly = measurementService.getEmployeeOfficeReport(ReportFrequency.MONTHLY);

        int dailyOfficeReport = userOfficeReportDaily.size();
        int weeklyReports = userOfficeReportWeekly.size();
        int monthlyReports = userOfficeReportMonthly.size();

        Assertions.assertEquals(dailyOfficeReport,userOfficeReportDaily.size() );
        Assertions.assertEquals(weeklyReports, userOfficeReportWeekly.size());
        Assertions.assertEquals(monthlyReports, userOfficeReportMonthly.size());

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user6", authorities = {"EMPLOYEE"})
    void testGetEmployeePublicRoomReport() {
        Sensor newSensor = new Sensor();
        newSensor.setSensorId("test_sensor");
        newSensor.setRoom(roomService.loadRoom(60));
        newSensor.setGasLimit(500);
        Sensor savedSensor = sensorService.saveSensor(newSensor);

        Measurement measurementToSave1 = new Measurement();
        measurementToSave1.setSensor(sensorService.loadSensor(savedSensor.getSensorId()));
        measurementToSave1.setTimestamp(Date.from(Instant.parse("2022-04-22T00:00:00.000Z")));
        measurementToSave1.setType(MeasurementType.AIR_QUALITY);
        measurementToSave1.setValue(100);
        measurementService.saveMeasurement(measurementToSave1);

        Measurement measurementToSave2 = new Measurement();
        measurementToSave2.setSensor(sensorService.loadSensor(savedSensor.getSensorId()));
        measurementToSave2.setTimestamp(Date.from(Instant.parse("2022-04-01T00:00:00.000Z")));
        measurementToSave2.setType(MeasurementType.AIR_QUALITY);
        measurementToSave2.setValue(80);
        measurementService.saveMeasurement(measurementToSave2);

        Measurement measurementToSave3 = new Measurement();
        measurementToSave3.setSensor(sensorService.loadSensor(savedSensor.getSensorId()));
        measurementToSave3.setTimestamp(Date.from(Instant.parse("2022-04-17T00:00:00.000Z")));
        measurementToSave3.setType(MeasurementType.AIR_QUALITY);
        measurementToSave3.setValue(99);
        measurementService.saveMeasurement(measurementToSave3);

        Map<Room,List<Measurement>> userPublicRoomReportDaily = measurementService.getEmployeePublicRoomReport(ReportFrequency.DAILY);
        Map<Room,List<Measurement>> userPublicRoomReportWeekly = measurementService.getEmployeePublicRoomReport(ReportFrequency.WEEKLY);
        Map<Room,List<Measurement>> userPublicRoomReportMonthly = measurementService.getEmployeePublicRoomReport(ReportFrequency.MONTHLY);

        Assertions.assertEquals(4, userPublicRoomReportDaily.size());
        Assertions.assertEquals(4, userPublicRoomReportWeekly.size());
        Assertions.assertEquals(4, userPublicRoomReportMonthly.size());

        //Assertions.assertEquals(100, Objects.requireNonNull(userPublicRoomReportDaily.keySet().stream().findFirst().orElse(null)).getValue());
        //Assertions.assertEquals(MeasurementType.AIR_QUALITY, Objects.requireNonNull(userPublicRoomReportDaily.keySet().stream().findFirst().orElse(null)).getType());
        //Assertions.assertEquals("2022-04-22 02:00:00.0", Objects.requireNonNull(userPublicRoomReportDaily.keySet().stream().findFirst().orElse(null)).getTimestamp().toString());
        //Assertions.assertEquals(savedSensor, Objects.requireNonNull(userPublicRoomReportDaily.keySet().stream().findFirst().orElse(null)).getSensor());

        Assertions.assertEquals("KITCHEN A", userPublicRoomReportDaily.keySet().stream().findFirst().orElse(null).getRoomName());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user6", authorities = {"EMPLOYEE"})
    void testGetEmployeePublicRoomReportForEmailSending() {
        Users users = userService.loadUser("user6");
        Sensor newSensor = new Sensor();
        newSensor.setSensorId("test_sensor");
        newSensor.setRoom(roomService.loadRoom(60));
        newSensor.setGasLimit(500);
        Sensor savedSensor = sensorService.saveSensor(newSensor);

        Measurement measurementToSave1 = new Measurement();
        measurementToSave1.setSensor(sensorService.loadSensor(savedSensor.getSensorId()));
        measurementToSave1.setTimestamp(Date.from(Instant.parse("2022-04-22T00:00:00.000Z")));
        measurementToSave1.setType(MeasurementType.AIR_QUALITY);
        measurementToSave1.setValue(100);
        measurementService.saveMeasurement(measurementToSave1);

        Measurement measurementToSave2 = new Measurement();
        measurementToSave2.setSensor(sensorService.loadSensor(savedSensor.getSensorId()));
        measurementToSave2.setTimestamp(Date.from(Instant.parse("2022-04-01T00:00:00.000Z")));
        measurementToSave2.setType(MeasurementType.AIR_QUALITY);
        measurementToSave2.setValue(80);
        measurementService.saveMeasurement(measurementToSave2);

        Measurement measurementToSave3 = new Measurement();
        measurementToSave3.setSensor(sensorService.loadSensor(savedSensor.getSensorId()));
        measurementToSave3.setTimestamp(Date.from(Instant.parse("2022-04-17T00:00:00.000Z")));
        measurementToSave3.setType(MeasurementType.AIR_QUALITY);
        measurementToSave3.setValue(99);
        measurementService.saveMeasurement(measurementToSave3);

        Map<Room,List<Measurement>> userPublicRoomReportDaily = measurementService.getEmployeePublicRoomReport(users, ReportFrequency.DAILY);
        Map<Room,List<Measurement>> userPublicRoomReportWeekly = measurementService.getEmployeePublicRoomReport(users,ReportFrequency.WEEKLY);
        Map<Room,List<Measurement>> userPublicRoomReportMonthly = measurementService.getEmployeePublicRoomReport(users,ReportFrequency.MONTHLY);

        Assertions.assertEquals(4, userPublicRoomReportDaily.size());
        Assertions.assertEquals(4, userPublicRoomReportWeekly.size());
        Assertions.assertEquals(4, userPublicRoomReportMonthly.size());

        //Assertions.assertEquals(100, Objects.requireNonNull(userPublicRoomReportDaily.keySet().stream().findFirst().orElse(null)).getValue());
        //Assertions.assertEquals(MeasurementType.AIR_QUALITY, Objects.requireNonNull(userPublicRoomReportDaily.keySet().stream().findFirst().orElse(null)).getType());
        //Assertions.assertEquals("2022-04-22 02:00:00.0", Objects.requireNonNull(userPublicRoomReportDaily.keySet().stream().findFirst().orElse(null)).getTimestamp().toString());
        //Assertions.assertEquals(savedSensor, Objects.requireNonNull(userPublicRoomReportDaily.keySet().stream().findFirst().orElse(null)).getSensor());

        Assertions.assertEquals("KITCHEN A", userPublicRoomReportDaily.keySet().stream().findFirst().orElse(null).getRoomName());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    void testGetDailyReport() {
        Department department1 = departmentService.loadDepartment(2);
        Collection<Room> publicRoomsInDep1 = roomService.getPublicRoomsPerDepartment(department1);

        Assertions.assertEquals(70, Objects.requireNonNull(publicRoomsInDep1.stream().findFirst().orElse(null)).getRoomId());
        publicRoomsInDep1.remove(roomService.loadRoom(40));
        Assertions.assertEquals(70, Objects.requireNonNull(publicRoomsInDep1.stream().findFirst().orElse(null)).getRoomId());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeleteMeasurement() {
        int allMeasurements = measurementService.getAllMeasurements().size();

        Assertions.assertEquals(allMeasurements ,measurementService.getAllMeasurements().size());

        Measurement measurement = measurementService.loadMeasurement(23);
            measurementService.deleteMeasurement(measurement);
            Assertions.assertNotEquals(allMeasurements ,measurementService.getAllMeasurements().size());


    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllMeasurementsByDepartment() {
        Department department = departmentService.loadDepartment(1);
        int daily = measurementService.getAllMeasurementsByDepartment(department, ReportFrequency.DAILY).size();
        int weekly = measurementService.getAllMeasurementsByDepartment(department, ReportFrequency.WEEKLY).size();
        int monthly = measurementService.getAllMeasurementsByDepartment(department, ReportFrequency.MONTHLY).size();
        Map<Room, List<Measurement>> departmentMeasurementDaily = measurementService.getAllMeasurementsByDepartment(department, ReportFrequency.DAILY);
        Map<Room, List<Measurement>>  departmentMeasurementWeekly = measurementService.getAllMeasurementsByDepartment(department,ReportFrequency.WEEKLY);
        Map<Room, List<Measurement>>  departmentMeasurementMonthly = measurementService.getAllMeasurementsByDepartment(department,ReportFrequency.MONTHLY);

        Assertions.assertEquals(daily, departmentMeasurementDaily.size());
        Assertions.assertEquals(weekly, departmentMeasurementWeekly.size());
        Assertions.assertEquals(monthly, departmentMeasurementMonthly.size());
    }





}
