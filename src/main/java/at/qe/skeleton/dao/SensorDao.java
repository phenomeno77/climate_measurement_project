package at.qe.skeleton.dao;

public class SensorDao {
    private final String sensor;

    public SensorDao(String sensor) {
        this.sensor = sensor;
    }

    public String getSensorId () {
        return sensor;
    }

    @Override
    public String toString() {
        return  sensor;

    }
}
