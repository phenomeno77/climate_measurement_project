package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope("view")
public class MeasurementListController {
    @Autowired
    private MeasurementService measurementService;
    @Autowired
    private SessionInfoBean sessionInfoBean;
    @Autowired
    private UserService userService;

    /**
     * Set Services for the JUnit tests
     *
     * @param userService
     * @param sessionInfoBean
     * @param measurementService
     */
    public void setServices(UserService userService, SessionInfoBean sessionInfoBean, MeasurementService measurementService) {
        this.sessionInfoBean = sessionInfoBean;
        this.measurementService = measurementService;
        this.userService = userService;
    }

    public List<Measurement> doGetAllMeasurements(ReportFrequency frequency){
       List<Measurement> sortedList = new ArrayList<>(measurementService.getAllMeasurements(frequency));
       sortedList.sort(Comparator.comparing(Measurement::getTimestamp));
        return sortedList;
    }

    public List<Measurement> doGetAllMeasurementsByDepartment(ReportFrequency frequency){
        Department department = userService.getAuthenticatedUser().getDepartment();
        Map<Room, List<Measurement>> measurementMapByDepartment = new HashMap<>(measurementService.getAllMeasurementsByDepartment(department,frequency));
        List<Measurement> measurementList = new ArrayList<>();

        for (Map.Entry<Room, List<Measurement>> entry : measurementMapByDepartment.entrySet()) {
            measurementList.addAll(entry.getValue());
        }

        measurementList.sort(Comparator.comparing(Measurement::getTimestamp));
        return measurementList;
    }

    public List<Measurement> doGetUsersReport(ReportFrequency frequency){
        Map<Room, List<Measurement>> measurementMapByEmployee= new HashMap<>(measurementService.getEmployeeRoomsReport( frequency));
        List<Measurement> measurementList = new ArrayList<>();

        for (Map.Entry<Room, List<Measurement>> entry : measurementMapByEmployee.entrySet()) {
            measurementList.addAll(entry.getValue());
        }

        measurementList.sort(Comparator.comparing(Measurement::getTimestamp));
        return measurementList;
    }


    public Measurement getMeasurementById(int id){
        return measurementService.loadMeasurement(id);
    }

    public void doDeleteMeasurement(Measurement measurement){
        measurementService.deleteMeasurement(measurement);
    }

    public List<Measurement> loadAllMeasurementsBySensor(Sensor sensor){
        return new ArrayList<>(measurementService.findMeasurementsBySensor(sensor));
    }

    public Map<Room, List<Measurement>>  doGetEmployeeReport(ReportFrequency frequency){
        return measurementService.getEmployeeRoomsReport( frequency);
    }


    public Map<Room, List<Measurement>> doGetAllMeasurementsAllRooms(ReportFrequency frequency){
        return measurementService.getAllMeasurementsAllRooms(frequency);
    }

    public Map<Room, List<Measurement>> doGetAllMeasurementsAllRoomsByDepartment(ReportFrequency frequency){
        Department department = userService.getAuthenticatedUser().getDepartment();
        return measurementService.getAllMeasurementsByDepartment(department,frequency);
    }

}
