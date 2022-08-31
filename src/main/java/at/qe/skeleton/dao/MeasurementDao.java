package at.qe.skeleton.dao;

public class MeasurementDao {
    private  double decibel;
    private  double lux;
    private  double temperature;
    private  double humidity;
    private  double pressure;
    private  double gas;
    private  String timestamp;

    public MeasurementDao(double decibel, double lux, double temperature, double humidity, double pressure, double gas, String timestamp) {
        this.decibel = decibel;
        this.lux = lux;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.gas = gas;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "MeasurementInformation{" +
                "decibel=" + decibel +
                ", lux=" + lux +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", gas=" + gas +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public double getDecibel() {
        return decibel;
    }

    public double getLux() {
        return lux;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public double getGas() {
        return gas;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
