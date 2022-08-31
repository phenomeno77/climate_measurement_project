package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.*;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@Scope("request")
public class GraphController{

    @Autowired
    UserService userService;

    @Autowired
    MeasurementService measurementService;

    /**Set Services for the JUnit tests
     *
     * @param userService
     * @param measurementService
     */
    public void setServices(UserService userService, MeasurementService measurementService) {
        this.userService = userService;
        this.measurementService = measurementService;
    }

    private static final String ROOM_TEMPERATURE = "Room Temperature";
    private static final String LEFT_Y_AXIS = "left-y-axis";
    private static final String BACKGROUND_COLOR = "#131313";
    private static final String A_68_A_7 = "#9A68A7";
    private static final String AIR_QUALITY = "Air Quality";
    private static final String RIGHT_Y_AXIS = "right-y-axis";
    private static final String AA_4_D = "#71AA4D";
    private static final String HUMIDITY = "Humidity";
    private static final String B_1_E_5 = "#39B1E5";
    private static final String NOISE_LEVEL = "Noise Level";
    private static final String FF_8_D_1_F = "#FF8D1F";
    private static final String LIGHT_INTENSITY = "Light Intensity";
    private static final String EF_457_E = "#EF457E";
    private static final String RIGHT = "right";
    private static final String LEFT = "left";
    private static final String PRESSURE = "Pressure";
    private static final String EF_483_E = "#EF483E";

    public List<LineChartModel> createGraphUserRoomsByInterval(ReportFrequency reportFrequency) {

        Map<Room, List<Measurement>> measurementMap = measurementService.getEmployeeRoomsReport(reportFrequency);
        List<LineChartModel> graphUserRooms = new ArrayList<>();
        for (Map.Entry<Room, List<Measurement>> entry : measurementMap.entrySet()) {

            graphUserRooms.add(createGraph(entry.getValue(),reportFrequency,entry.getKey().getRoomName()));
        }
        return graphUserRooms;
    }

    public List<LineChartModel> createGraphAllRooms(ReportFrequency reportFrequency) {
        Map<Room, List<Measurement>> measurementMap = measurementService.getAllMeasurementsAllRooms(reportFrequency);
        List<LineChartModel> allRooms = new ArrayList<>();
        for (Map.Entry<Room, List<Measurement>> entry : measurementMap.entrySet()) {
            allRooms.add(createGraph(entry.getValue(),reportFrequency,entry.getKey().getRoomName()));
        }
        return allRooms;
    }

    public List<LineChartModel> createGraphAllRoomsByDepartment(ReportFrequency reportFrequency) {
        Department department = userService.getAuthenticatedUser().getDepartment();
        Map<Room, List<Measurement>> measurementMap = measurementService.getAllMeasurementsByDepartment(department,reportFrequency);
        List<LineChartModel> allRoomsByDepartment = new ArrayList<>();
        for (Map.Entry<Room, List<Measurement>> entry : measurementMap.entrySet()) {
            allRoomsByDepartment.add(createGraph(entry.getValue(),reportFrequency,entry.getKey().getRoomName()));
        }
        return allRoomsByDepartment;
    }

    private LineChartModel createGraph(List<Measurement> measurementList, ReportFrequency reportFrequency,String roomName){
        LineChartModel graphOffice = new LineChartModel();
        ChartData data = new ChartData();
        List<String> labels = new ArrayList<>();

        Collections.sort(measurementList);
        LineChartDataSet temperatureDataSet = new LineChartDataSet();
        List<Object> temperatureValues = new ArrayList<>();
        LineChartDataSet airQualityDataSet = new LineChartDataSet();
        List<Object> airQualityValues = new ArrayList<>();
        LineChartDataSet humidityDataSet = new LineChartDataSet();
        List<Object> humidityValues = new ArrayList<>();
        LineChartDataSet noiseDataSet = new LineChartDataSet();
        List<Object> noiseValues = new ArrayList<>();
        LineChartDataSet lightIntensityDataSet = new LineChartDataSet();
        List<Object> lightIntensityValues = new ArrayList<>();
        List<Object> pressureValues = new ArrayList<>();
        LineChartDataSet pressureDataSet = new LineChartDataSet();
        for (Measurement measurement : measurementList) {
            if (measurement.getType() == MeasurementType.TEMPERATURE) {
                temperatureValues.add(measurement.getValue());
                labels.add(measurement.getTimestamp().toString());
            }
            if (measurement.getType() == MeasurementType.GAS) {
                airQualityValues.add(measurement.getValue());
            }
            if (measurement.getType() == MeasurementType.HUMIDITY) {
                humidityValues.add(measurement.getValue());
            }
            if (measurement.getType() == MeasurementType.DECIBEL) {
                noiseValues.add(measurement.getValue());
            }
            if (measurement.getType() == MeasurementType.LUX) {
                lightIntensityValues.add(measurement.getValue());
            }
            if (measurement.getType() == MeasurementType.PRESSURE) {
                pressureValues.add(measurement.getValue());
            }
        }
        temperatureDataSet.setData(temperatureValues);
        temperatureDataSet.setLabel(ROOM_TEMPERATURE);
        temperatureDataSet.setYaxisID(LEFT_Y_AXIS);
        temperatureDataSet.setFill(true);
        temperatureDataSet.setBackgroundColor(BACKGROUND_COLOR);
        temperatureDataSet.setBorderColor(A_68_A_7);
        temperatureDataSet.setLineTension(0.5);
        airQualityDataSet.setData(airQualityValues);
        airQualityDataSet.setLabel(AIR_QUALITY);
        airQualityDataSet.setYaxisID(RIGHT_Y_AXIS);
        airQualityDataSet.setBackgroundColor(BACKGROUND_COLOR);
        airQualityDataSet.setBorderColor(AA_4_D);
        airQualityDataSet.setFill(true);
        airQualityDataSet.setLineTension(0.5);
        humidityDataSet.setData(humidityValues);
        humidityDataSet.setLabel(HUMIDITY);
        humidityDataSet.setBackgroundColor(BACKGROUND_COLOR);
        humidityDataSet.setBorderColor(B_1_E_5);
        humidityDataSet.setFill(true);
        humidityDataSet.setHidden(true);
        humidityDataSet.setLineTension(0.5);
        noiseDataSet.setData(noiseValues);
        noiseDataSet.setLabel(NOISE_LEVEL);
        noiseDataSet.setBackgroundColor(BACKGROUND_COLOR);
        noiseDataSet.setBorderColor(FF_8_D_1_F);
        noiseDataSet.setFill(true);
        noiseDataSet.setHidden(true);
        noiseDataSet.setLineTension(0.5);
        lightIntensityDataSet.setData(lightIntensityValues);
        lightIntensityDataSet.setLabel(LIGHT_INTENSITY);
        lightIntensityDataSet.setFill(true);
        lightIntensityDataSet.setHidden(true);
        lightIntensityDataSet.setBackgroundColor(BACKGROUND_COLOR);
        lightIntensityDataSet.setBorderColor(EF_457_E);
        lightIntensityDataSet.setLineTension(0.5);

        pressureDataSet.setData(lightIntensityValues);
        pressureDataSet.setLabel(PRESSURE);
        pressureDataSet.setFill(true);
        pressureDataSet.setHidden(true);
        pressureDataSet.setBackgroundColor(BACKGROUND_COLOR);
        pressureDataSet.setBorderColor(EF_483_E);
        pressureDataSet.setLineTension(0.5);

        data.addChartDataSet(temperatureDataSet);
        data.addChartDataSet(airQualityDataSet);
        data.addChartDataSet(humidityDataSet);
        data.addChartDataSet(noiseDataSet);
        data.addChartDataSet(lightIntensityDataSet);
        data.addChartDataSet(pressureDataSet);
        data.setLabels(labels);
        graphOffice.setData(data);
        //Options
        LineChartOptions options = new LineChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setId(LEFT_Y_AXIS);
        linearAxes.setPosition(LEFT);
        CartesianLinearAxes linearAxes2 = new CartesianLinearAxes();
        linearAxes2.setId(RIGHT_Y_AXIS);
        linearAxes2.setPosition(RIGHT);
        cScales.addYAxesData(linearAxes);
        cScales.addYAxesData(linearAxes2);
        options.setScales(cScales);
        Title title = new Title();
        title.setDisplay(true);

        title.setText(reportFrequency.name() + " Measurement Chart " + roomName);
        options.setTitle(title);
        graphOffice.setOptions(options);
        return graphOffice;
    }

}