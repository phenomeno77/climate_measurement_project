package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.MeasurementType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class AverageCalculatorReport {

    private double temperature =0;
    private double humidity =0;
    private double lux =0;
    private double gas =0;
    private double decibel =0;
    private double pressure =0;

    public void calculateAverages(List<Measurement> measurementList){
        temperature = measurementList.stream()
                .filter(type -> type.getType().equals(MeasurementType.TEMPERATURE))
                .mapToDouble(Measurement::getValue)
                .average()
                .orElse(0.0);
        humidity = measurementList.stream()
                .filter(type -> type.getType().equals(MeasurementType.HUMIDITY))
                .mapToDouble(Measurement::getValue)
                .average()
                .orElse(0.0);
        lux = measurementList.stream()
                .filter(type -> type.getType().equals(MeasurementType.LUX))
                .mapToDouble(Measurement::getValue)
                .average()
                .orElse(0.0);
        gas = measurementList.stream()
                .filter(type -> type.getType().equals(MeasurementType.GAS))
                .mapToDouble(Measurement::getValue)
                .average()
                .orElse(0.0);
        decibel = measurementList.stream()
                .filter(type -> type.getType().equals(MeasurementType.DECIBEL))
                .mapToDouble(Measurement::getValue)
                .average()
                .orElse(0.0);
        pressure = measurementList.stream()
                .filter(type -> type.getType().equals(MeasurementType.PRESSURE))
                .mapToDouble(Measurement::getValue)
                .average()
                .orElse(0.0);
    }

    public double getTemperature() {
        BigDecimal bd = BigDecimal.valueOf(temperature).setScale(2, RoundingMode.HALF_UP);

        return  bd.doubleValue();
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        BigDecimal bd = BigDecimal.valueOf(humidity).setScale(2, RoundingMode.HALF_UP);

        return  bd.doubleValue();
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getLux() {
        BigDecimal bd = BigDecimal.valueOf(lux).setScale(2, RoundingMode.HALF_UP);

        return  bd.doubleValue();
    }

    public void setLux(double lux) {
        this.lux = lux;
    }

    public double getGas() {
        BigDecimal bd = BigDecimal.valueOf(gas).setScale(2, RoundingMode.HALF_UP);

        return  bd.doubleValue();
    }

    public void setGas(double gas) {
        this.gas = gas;
    }

    public double getDecibel() {
        BigDecimal bd = BigDecimal.valueOf(decibel).setScale(2, RoundingMode.HALF_UP);

        return  bd.doubleValue();
    }

    public void setDecibel(double decibel) {
        this.decibel = decibel;
    }

    public double getPressure() {
        BigDecimal bd = BigDecimal.valueOf(pressure).setScale(2, RoundingMode.HALF_UP);

        return  bd.doubleValue();
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "AverageCalculatorReport{" +
                "tempAverage=" + temperature +
                ", humidityAverage=" + humidity +
                ", luxAverage=" + lux +
                ", gasAverage=" + gas +
                ", decibelAverage=" + decibel +
                ", pressureAverage=" + pressure +
                '}';
    }
}
