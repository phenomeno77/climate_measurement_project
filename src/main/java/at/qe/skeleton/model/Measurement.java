package at.qe.skeleton.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "measurement")
public class Measurement implements Persistable<String>, Serializable, Comparable<Measurement> {
    @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int measurementId;
    @Enumerated(EnumType.STRING)
    private MeasurementType type;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    private double value;


    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = true)
    private Sensor sensor;

    public int getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(int measurementId) {
        this.measurementId = measurementId;
    }


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public MeasurementType getType() {
        return type;
    }

    public void setType(MeasurementType type) {
        this.type = type;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public int compareTo(Measurement o) {
        return Integer.compare(this.measurementId,o.getMeasurementId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Measurement)) {
            return false;
        }
        final Measurement other = (Measurement) obj;
        return Objects.equals(this.measurementId,other.measurementId)
                && Objects.equals(this.sensor.getSensorId(),other.sensor.getSensorId())
                && Objects.equals(this.timestamp,other.timestamp)
                && Objects.equals(this.type,other.type)
                && Objects.equals(this.value,other.value);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.measurementId);
        return hash;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "measurementId=" + measurementId +
                ", type=" + type +
                ", timestamp=" + timestamp +
                ", value=" + value +
                ", sensor=" + sensor +
                '}';
    }


}
