package at.qe.skeleton.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RoomInformationDao {

    private final double noise;
    private final double bright;
    private final double tempHot;
    private final double tempCold;
    private final double humLow;
    private final double humHigh;
    private final double presHigh;
    private final double presLow;


    private final double gas;
    private final SensorDao sensor;
    private final RoomDao room;
    private final Collection<MeasurementDao> roomInfo;

    public RoomInformationDao(float noise, float bright, float tempHot, float tempCold,
                              float humLow, float humHigh, float presHigh, float presLow, float gas,
                              SensorDao sensor, RoomDao room, Collection<MeasurementDao> roomInfo) {
        this.noise = noise;
        this.bright = bright;
        this.tempHot = tempHot;
        this.tempCold = tempCold;
        this.humLow = humLow;
        this.humHigh = humHigh;
        this.presHigh = presHigh;
        this.presLow = presLow;
        this.gas = gas;
        this.sensor = sensor;
        this.room = room;
        this.roomInfo = roomInfo;
    }


    @Override
    public String toString() {
        return "RoomInformationDao{" +
                "noise=" + noise +
                ", bright=" + bright +
                ", tempHot=" + tempHot +
                ", tempCold=" + tempCold +
                ", humLow=" + humLow +
                ", humHigh=" + humHigh +
                ", presHigh=" + presHigh +
                ", presLow=" + presLow +
                ", gas=" + gas +
                ", sensor=" + sensor +
                ", room=" + room +
                ", roomInfo=" + roomInfo +
                '}';
    }

    public SensorDao getSensor() {
        return sensor;
    }

    public Map<String, Double> getLimits(){
    HashMap<String, Double> limits = new HashMap<>();
        limits.put("noise", noise);
        limits.put("bright", bright);
        limits.put("tempHot",tempHot);
        limits.put("tempCold",tempCold);
        limits.put("humLow",humLow);
        limits.put("humHigh",humHigh);
        limits.put("presHigh",presHigh);
        limits.put("presLow",presLow);
        limits.put("gas",gas);

        return limits;
    }

    public double getNoise() {
        return noise;
    }

    public double getBright() {
        return bright;
    }

    public double getTempHot() {
        return tempHot;
    }

    public double getTempCold() {
        return tempCold;
    }

    public double getHumLow() {
        return humLow;
    }

    public double getHumHigh() {
        return humHigh;
    }

    public double getPresHigh() {
        return presHigh;
    }

    public double getPresLow() {
        return presLow;
    }

    public double getGas() {
        return gas;
    }

    public RoomDao getRoom() {
        return room;
    }

    public Collection<MeasurementDao> getRoomInfo() {
        return roomInfo;
    }
}
