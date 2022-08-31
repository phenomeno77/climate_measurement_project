package at.qe.skeleton.services;

import at.qe.skeleton.dao.MeasurementDao;
import at.qe.skeleton.dao.RoomInformationDao;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.ui.beans.AverageCalculatorReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Service for accessing and manipulating measurements.
 */
@Transactional
@Service
public class MeasurementService {
    @Autowired
    private final MeasurementRepository measurementRepository;

    @Autowired
    SensorService sensorService;

    @Autowired
    UserService userService;

    @Autowired
    RoomService roomService;
    @Autowired
    AverageCalculatorReport averageCalculatorReport;


    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;

    }

    /**
     * Returns a collection of all measurements.
     *
     * @return all stored measurements
     */
    public Collection<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }

    /**
     * Loads a single measurement identified by its id.
     *
     * @param id the id of the given measurement to search for
     * @return the measurement with the given id
     */
    public Measurement loadMeasurement(int id) {
        return measurementRepository.findById(id).orElse(null);
    }

    /**
     * Saves the measurement.
     *
     * @param measurement the measurement to save
     * @return the updated/saved measurement
     */
    public Measurement saveMeasurement(Measurement measurement) {

        Room measurementRoom = measurement.getSensor().getRoom();

        if (measurementRoom.getRoomType().equals(RoomType.OFFICE)  //1. check if the room is an office or not
                &&
                measurementRoom.getNumberOfSeats() > 4   //2. check if the room has a capacity of 5 more seats
                &&
                ((roomService.getPresentUsersByOffice(measurementRoom).size() > 4) //3. check if there are >=5 present people in the office
                        ||
                        (roomService.getPresentUsersByOffice(measurementRoom).isEmpty()))) { //3. check if there are no people in the office
            return measurementRepository.save(measurement);
        }

        //else room is public, save the measurement
        if (!measurementRoom.getRoomType().equals(RoomType.OFFICE)) {
            return measurementRepository.save(measurement);
        }

        return new Measurement();
    }

    /**
     * Deletes the measurement.
     *
     * @param measurement the measurement to delete
     */
    @Transactional
    public void deleteMeasurement(Measurement measurement) {

        measurementRepository.deleteMeasurement(measurement.getMeasurementId());
    }

    public Collection<Measurement> findMeasurementsBySensor(Sensor sensor) {
        return measurementRepository.findMeasurementsBySensor(sensor);
    }

    public Collection<Measurement> getEmployeeOfficeReport(ReportFrequency frequency) {
        Users authenticatedUsers = userService.getAuthenticatedUser();
        return getAllMeasurementsByRoom(authenticatedUsers.getOffice(), frequency);
    }

    public Map<Room, List<Measurement>>  getEmployeeRoomsReport(ReportFrequency frequency) {
            Map<Room, List<Measurement>> report = getEmployeePublicRoomReport(userService.getAuthenticatedUser(),frequency);
            for (Room room : roomService.getAllOffices()) {
                if (room.getEmployees().contains(userService.getAuthenticatedUser())) {
                    List<Measurement> measurementList = new ArrayList<>(getAllMeasurementsByRoom(room, frequency));
                    report.put(room, measurementList);
                }
            }
            return report;
    }

    public Collection<Measurement> getEmployeeOfficeReport(Users user, ReportFrequency frequency) {

        return getAllMeasurementsByRoom(user.getOffice(), frequency);
    }

    public void mapMeasurementData(RoomInformationDao roomInformation) {
        List<Measurement> measurements = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");

        Sensor sensor = sensorService.loadSensor(roomInformation.getSensor().getSensorId());

        for (MeasurementDao measurement : roomInformation.getRoomInfo()) {

            ReflectionUtils.doWithFields(measurement.getClass(), field -> {
                Measurement measurementToDb = new Measurement();
                measurementToDb.setSensor(sensor);
                field.setAccessible(true);

                if (!field.getName().equals("timestamp")) {

                    measurementToDb.setType(MeasurementType.valueOf(field.getName().toUpperCase(Locale.ROOT)));
                    Double value = (Double) field.get(measurement);
                    measurementToDb.setValue(Double.parseDouble(df.format(value)));
                    measurements.add(measurementToDb);
                }
            });
        }

        averageCalculatorReport.calculateAverages(measurements);
        saveAverageMeasurements(sensor);

    }

    private void saveAverageMeasurements(Sensor sensor) {
        DecimalFormat df = new DecimalFormat("#.##");

        ReflectionUtils.doWithFields(averageCalculatorReport.getClass(), field -> {
            Measurement measurementToDb = new Measurement();
            measurementToDb.setSensor(sensor);
            field.setAccessible(true);


            measurementToDb.setType(MeasurementType.valueOf(field.getName().toUpperCase(Locale.ROOT)));
            Double value = (Double) field.get(averageCalculatorReport);
            measurementToDb.setValue(Double.parseDouble(df.format(value)));

            measurementToDb.setTimestamp(new Date(System.currentTimeMillis()));
            this.saveMeasurement(measurementToDb);

        });
    }

    public Map<Room, List<Measurement>> getEmployeePublicRoomReport(ReportFrequency frequency) {

        Map<Room, List<Measurement>> report = new HashMap<>();
        Users authenticatedUsers = userService.getAuthenticatedUser();
        Collection<Room> publicRoomsInDepartment = roomService.getPublicRoomsPerDepartment(authenticatedUsers.getDepartment());
        List<Measurement> measurementList = new ArrayList<>();
        Date timeLimit = calculateTimeLimit(frequency);

        for (Room room : publicRoomsInDepartment) {
            Set<Sensor> sensorInPublicRoom = room.getSensors();
            for (Sensor sensor : sensorInPublicRoom) {
                Collection<Measurement> measurements = findMeasurementsBySensor(sensor);
                measurementList = new ArrayList<>();
                for (Measurement measurement : measurements) {
                    if (measurement.getTimestamp().after(timeLimit)) {
                        measurementList.add(measurement);
                    }
                }
            }
            report.put(room, measurementList);
        }

        return report;
    }

    public Map<Room, List<Measurement>> getEmployeePublicRoomReport(Users user, ReportFrequency frequency) {
        Map<Room, List<Measurement>> report = new HashMap<>();
        Collection<Room> publicRoomsInDepartment = roomService.getPublicRoomsPerDepartment(user.getDepartment());
        List<Measurement> measurementList = new ArrayList<>();
        Date timeLimit = calculateTimeLimit(frequency);

        for (Room room : publicRoomsInDepartment) {
            Set<Sensor> sensorInPublicRoom = room.getSensors();
            for (Sensor sensor : sensorInPublicRoom) {
                Collection<Measurement> measurements = findMeasurementsBySensor(sensor);
                measurementList = new ArrayList<>();
                for (Measurement measurement : measurements) {
                    if (measurement.getTimestamp().after(timeLimit)) {
                        measurementList.add(measurement);
                    }
                }
            }
            report.put(room, measurementList);
        }

        return report;
    }


    public List<Measurement> getAllMeasurementsByRoom(Room room, ReportFrequency frequency) {
        Collection<Measurement> measurements = getAllRoomMeasurements(room);
        List<Measurement> report = new ArrayList<>();
        for (Measurement measurement : measurements) {
            if (measurement.getTimestamp().after(calculateTimeLimit(frequency))) {
                report.add(measurement);
            }
        }
        return report;
    }


    public Map<Room, List<Measurement>> getAllMeasurementsByDepartment(Department department, ReportFrequency frequency) {
        Map<Room, List<Measurement>> report = new HashMap<>();
        for(Room room : roomService.getRoomsByDepartmentManager(department.getDepartmentManager())){
            List<Measurement> measurementList = new ArrayList<>(getAllMeasurementsByRoom(room,frequency));
            report.put(room,measurementList);
        }
        return report;
    }

    public Map<Room, List<Measurement>> getAllMeasurementsAllRooms(ReportFrequency frequency) {
        Map<Room, List<Measurement>> report = new HashMap<>();
        for (Room room : roomService.getAllRooms()) {
            List<Measurement> measurementList = new ArrayList<>(getAllMeasurementsByRoom(room,frequency));
            report.put(room,measurementList);
        }
        return report;
    }

    public Collection<Measurement> getAllMeasurements(ReportFrequency frequency) {
        Collection<Measurement> measurements = getAllMeasurements();
        Collection<Measurement> allMeasurements = new ArrayList<>();
        for (Measurement measurement : measurements) {
            if (measurement.getTimestamp().after(calculateTimeLimit(frequency))) {
                allMeasurements.add(measurement);
            }
        }
        return allMeasurements;
    }

    public Map<Measurement, String> getAllPublicRoomMeasurementsByDepartment(Department department, ReportFrequency frequency) {
        Map<Measurement, String> report = new HashMap<>();
        Collection<Measurement> measurements = getAllPublicRoomMeasurementsPerDepartment(department);
        for (Measurement measurement : measurements) {
            if (measurement.getTimestamp().after(calculateTimeLimit(frequency))) {
                report.put(measurement, measurement.getSensor().getRoom().getRoomName());
            }
        }
        return report;
    }

    public Map<Measurement, String> getAllPublicRoomMeasurements(ReportFrequency frequency) {
        Map<Measurement, String> report = new HashMap<>();
        Collection<Measurement> measurements = getAllPublicRoomMeasurements();
        for (Measurement measurement : measurements) {
            if (measurement.getTimestamp().after(calculateTimeLimit(frequency))) {
                report.put(measurement, measurement.getSensor().getRoom().getRoomName());
            }
        }
        return report;
    }


    private Date calculateTimeLimit(ReportFrequency frequency) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (frequency == ReportFrequency.WEEKLY) {
            c.add(Calendar.DATE, -7);
        } else if (frequency == ReportFrequency.MONTHLY) {
            c.add(Calendar.MONTH, -1);
        } else {
            c.add(Calendar.HOUR, -24);
        }
        return c.getTime();
    }

    public Collection<Measurement> getAllRoomMeasurements(Room room) {
        Collection<Measurement> measurements = new HashSet<>();
        Collection<Sensor> sensors = sensorService.getAllSensorsByRoom(room);

        for (Sensor sensor : sensors) {
            Collection<Measurement> loadedMeasurements = findMeasurementsBySensor(sensor);
            measurements.addAll(loadedMeasurements);
        }
        return measurements;
    }

    private Collection<Measurement> getAllPublicRoomMeasurements() {
        Collection<Measurement> measurements = getAllMeasurements();
        Collection<Measurement> roomMeasurements = new HashSet<>();
        for (Measurement measurement : measurements) {
            if (measurement.getSensor().getRoom().getRoomType() != RoomType.OFFICE) {
                roomMeasurements.add(measurement);
            }
        }
        return roomMeasurements;
    }

    private Collection<Measurement> getAllPublicRoomMeasurementsPerDepartment(Department department) {
        Collection<Measurement> measurements = getAllPublicRoomMeasurements();
        Collection<Measurement> departmentMeasurements = new HashSet<>();
        for (Measurement measurement : measurements) {
            if (measurement.getSensor().getRoom().getDepartment().getDepartmentId() == department.getDepartmentId()) {
                departmentMeasurements.add(measurement);
            }
        }
        return departmentMeasurements;
    }
}
