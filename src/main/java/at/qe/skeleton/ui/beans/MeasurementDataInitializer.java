package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.*;

@Component
@Scope("request")
public class MeasurementDataInitializer {

    @Autowired
    MeasurementService measurementService;
    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;

    public void setServices(MeasurementService measurementService, UserService userService, RoomService roomService) {
    this.measurementService = measurementService;
    this.userService = userService;
    this.roomService = roomService;
    }

    private Measurement measurement;


    public void selectDayInterval(int dayInterval) throws IOException {
        initializeMeasurementDataForInterval(dayInterval);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/secured/dashboard.xhtml");
    }

    public void initializeMeasurementDataForInterval(int dayInterval){
        Set<Sensor> sensors = userService.getAuthenticatedUser().getOffice().getSensors();
        Collection<Room> publicRoomsByDepartment = roomService.getPublicRoomsPerDepartment(userService.getAuthenticatedUser().getDepartment());
        if(!sensors.isEmpty()) {
            for (Sensor sensorOffice : sensors) {
                initializeMeasurementBySensor(dayInterval,sensorOffice);
            }
            }

        if (!publicRoomsByDepartment.isEmpty()) {
            for (Room publicRoom : publicRoomsByDepartment) {
                for (Sensor publicSensor : publicRoom.getSensors()) {
                    initializeMeasurementBySensor(dayInterval,publicSensor);
                }
            }
        }
    }

    public void initializeMeasurementBySensor(int dayInterval, Sensor sensorOffice){

                Date timestamp;
                Calendar cal = Calendar.getInstance();

                cal.add(Calendar.DATE, -dayInterval);
                cal.set(Calendar.HOUR_OF_DAY,0);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);

        for (int day = 0; day < dayInterval; day++) {
            for(int hour =0; hour < 24; hour++) {
                cal.add(Calendar.HOUR, 1);
                if (hour < 8) {
                    timestamp = cal.getTime();
                    createTemp(5 + (double) hour, timestamp, sensorOffice);
                    createGas(15 + (double) hour, timestamp, sensorOffice);
                    createDecibel((double) hour + 5, timestamp, sensorOffice);
                    createLux(timestamp, sensorOffice);
                    createHumidity(33 + ((double) hour / 10), timestamp, sensorOffice);
                    createPressure(timestamp, sensorOffice);
                } else if (hour < 16) {
                    timestamp = cal.getTime();
                    createTemp(10 + (double) hour, timestamp, sensorOffice);
                    createGas(15 + (double) hour, timestamp, sensorOffice);
                    createDecibel((double) hour + 5, timestamp, sensorOffice);
                    createLux(timestamp, sensorOffice);
                    createHumidity(33 + ((double) hour / 5), timestamp, sensorOffice);
                    createPressure(timestamp, sensorOffice);
                } else {
                    timestamp = cal.getTime();
                    createTemp(24 - ((double) hour - 16), timestamp, sensorOffice);
                    createGas(15 + (double) hour, timestamp, sensorOffice);
                    createDecibel((double) hour + 10, timestamp, sensorOffice);
                    createLux(timestamp, sensorOffice);
                    createHumidity(34 - ((double) hour / 10), timestamp, sensorOffice);
                    createPressure(timestamp, sensorOffice);
                }
            }
        }

    }

    public void createTemp(double value, Date timestamp,Sensor sensor){
        measurement = new Measurement();
        measurement.setValue(value);
        measurement.setType(MeasurementType.TEMPERATURE);
        measurement.setSensor(sensor);
        measurement.setTimestamp(timestamp);
        measurementService.saveMeasurement(measurement);

    }

    public void createGas(double value, Date timestamp,Sensor sensor){
        measurement = new Measurement();
        measurement.setValue(value);
        measurement.setType(MeasurementType.GAS);
        measurement.setSensor(sensor);
        measurement.setTimestamp(timestamp);
        measurementService.saveMeasurement(measurement);
    }

    public void createDecibel(double value, Date timestamp,Sensor sensor){
        measurement = new Measurement();
        measurement.setValue(value);
        measurement.setType(MeasurementType.DECIBEL);
        measurement.setSensor(sensor);
        measurement.setTimestamp(timestamp);
        measurementService.saveMeasurement(measurement);
    }

    public void createHumidity(double value,Date timestamp, Sensor sensor){
        measurement = new Measurement();
        measurement.setValue(value);
        measurement.setType(MeasurementType.HUMIDITY);
        measurement.setSensor(sensor);
        measurement.setTimestamp(timestamp);
        measurementService.saveMeasurement(measurement);
    }

    public void createPressure(Date timestamp, Sensor sensor){
        measurement = new Measurement();
        measurement.setValue(952.52);
        measurement.setType(MeasurementType.PRESSURE);
        measurement.setSensor(sensor);
        measurement.setTimestamp(timestamp);
        measurementService.saveMeasurement(measurement);
    }

    public void createLux(Date timestamp, Sensor sensor){
        measurement = new Measurement();
        measurement.setValue(2.0);
        measurement.setType(MeasurementType.LUX);
        measurement.setSensor(sensor);
        measurement.setTimestamp(timestamp);
        measurementService.saveMeasurement(measurement);
    }
}
