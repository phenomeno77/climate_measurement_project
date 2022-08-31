package at.qe.skeleton.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "sensor")
public class Sensor implements Persistable<String>, Serializable, Comparable<Sensor> {

    @Id
    @Column(length = 100)
    private String sensorId;

    @Column(nullable = true)
    private double decibelLimit;
    @Column(nullable = true)
    private double luxLimit;
    @Column(nullable = true)
    private double temperatureLimitHigh;
    @Column(nullable = true)
    private double temperatureLimitLow;
    @Column(nullable = true)
    private double humidityLimitHigh;
    @Column(nullable = true)
    private double humidityLimitLow;
    @Column(nullable = true)
    private double pressureLimitHigh;
    @Column(nullable = true)
    private double pressureLimitLow;
    @Column(nullable = true)

    private double gasLimit;

    public Sensor() {
    }

    public Sensor(double decibelLimit, double luxLimit, double temperatureLimitHigh, double temperatureLimitLow, double humidityLimitHigh, double humidityLimitLow, double pressureLimitHigh, double pressureLimitLow, double gasLimit) {
        this.decibelLimit = decibelLimit;
        this.luxLimit = luxLimit;
        this.temperatureLimitHigh = temperatureLimitHigh;
        this.temperatureLimitLow = temperatureLimitLow;
        this.humidityLimitHigh = humidityLimitHigh;
        this.humidityLimitLow = humidityLimitLow;
        this.pressureLimitHigh = pressureLimitHigh;
        this.pressureLimitLow = pressureLimitLow;
        this.gasLimit = gasLimit;
    }

    /*1 distinct Sensor is only in one Room*/
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    private Room room;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private Set<Measurement> measurements;

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Set<Measurement> getMeasurements() {
        return measurements;
    }

    public double getDecibelLimit() {
        return decibelLimit;
    }

    public void setDecibelLimit(double decibelLimit) {
        this.decibelLimit = decibelLimit;
    }

    public double getLuxLimit() {
        return luxLimit;
    }

    public void setLuxLimit(double luxLimit) {
        this.luxLimit = luxLimit;
    }

    public double getTemperatureLimitHigh() {
        return temperatureLimitHigh;
    }

    public void setTemperatureLimitHigh(double temperatureLimitHigh) {
        this.temperatureLimitHigh = temperatureLimitHigh;
    }

    public double getTemperatureLimitLow() {
        return temperatureLimitLow;
    }

    public void setTemperatureLimitLow(double temperatureLimitLow) {
        this.temperatureLimitLow = temperatureLimitLow;
    }

    public double getHumidityLimitHigh() {
        return humidityLimitHigh;
    }

    public void setHumidityLimitHigh(double humidityLimitHigh) {
        this.humidityLimitHigh = humidityLimitHigh;
    }

    public double getHumidityLimitLow() {
        return humidityLimitLow;
    }

    public void setHumidityLimitLow(double humidityLimitLow) {
        this.humidityLimitLow = humidityLimitLow;
    }

    public double getPressureLimitHigh() {
        return pressureLimitHigh;
    }

    public void setPressureLimitHigh(double pressureLimitHigh) {
        this.pressureLimitHigh = pressureLimitHigh;
    }

    public double getPressureLimitLow() {
        return pressureLimitLow;
    }

    public void setPressureLimitLow(double pressureLimitLow) {
        this.pressureLimitLow = pressureLimitLow;
    }

    public double getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(double gasLimit) {
        this.gasLimit = gasLimit;
    }

    public void setMeasurements(Set<Measurement> measurements) {
        this.measurements = measurements;
    }




    public void setLimits(Map<String, Double> limits) {
        this.decibelLimit = limits.get("noise");
        this.luxLimit = limits.get("bright");
        this.temperatureLimitHigh = limits.get("tempHot");
        this.temperatureLimitLow = limits.get("tempCold");
        this.humidityLimitHigh = limits.get("humHigh");
        this.humidityLimitLow = limits.get("humLow");
        this.pressureLimitHigh = limits.get("presHigh");
        this.pressureLimitLow = limits.get("presLow");
        this.gasLimit = limits.get("gas");

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.sensorId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Sensor)) {
            return false;
        }
        final Sensor other = (Sensor) obj;
        return Objects.equals(this.sensorId, other.sensorId);
    }

    @Override
    public int compareTo(Sensor o) {
        return this.sensorId.compareTo(o.sensorId);
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}

