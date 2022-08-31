package at.qe.skeleton.tests.DAO_Models;

import at.qe.skeleton.dao.MeasurementDao;
import at.qe.skeleton.dao.RoomDao;
import at.qe.skeleton.dao.RoomInformationDao;
import at.qe.skeleton.dao.SensorDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Collection;

@SpringBootTest
@WebAppConfiguration
public class DAOModelTests {

    @Test
    void MeasurementDaoTest(){
          double decibel = 20.0;
          double lux= 20.0;
          double temperature= 20.0;
          double humidity= 20.0;
          double pressure= 20.0;
          double gas= 20.0;
          String timestamp = "10-10-2010 00:00:00";

        MeasurementDao measurementDao = new MeasurementDao(decibel,lux,temperature,humidity,pressure,gas,timestamp);

        Assertions.assertEquals(decibel,measurementDao.getDecibel());
        Assertions.assertEquals(lux,measurementDao.getLux());
        Assertions.assertEquals(temperature,measurementDao.getTemperature());
        Assertions.assertEquals(humidity,measurementDao.getHumidity());
        Assertions.assertEquals(pressure,measurementDao.getPressure());
        Assertions.assertEquals(gas,measurementDao.getGas());
        Assertions.assertEquals(timestamp,measurementDao.getTimestamp());
    }

    @Test
    void RoomDaoTest(){
       String room = "TestRoom";
        RoomDao roomDao = new RoomDao(room);

        Assertions.assertEquals(room,roomDao.getRoomName());
    }


    @Test
    void SensorDaoTest(){
        String sensor = "TestSensor";
        SensorDao sensorDao = new SensorDao(sensor);

        Assertions.assertEquals(sensor,sensorDao.getSensorId());
    }

    @Test
    void RoomInformationDaoTest(){


        float noise = 5.0f;
        float bright= 5.0f;
        float tempHot= 5.0f;
        float tempCold= 5.0f;
        float humLow= 5.0f;
        float humHigh= 5.0f;
        float presHigh= 5.0f;
        float presLow= 5.0f;
        float gas = 5.0f;
        String sensor = "TestSensor";
        SensorDao sensorDao = new SensorDao(sensor);
        String room = "TestRoom";
        RoomDao roomDao = new RoomDao(room);
        double decibel = 20.0;
        double lux= 20.0;
        double temperature= 20.0;
        double humidity= 20.0;
        double pressure= 20.0;
        double gass= 20.0;
        String timestamp = "10-10-2010 00:00:00";
        MeasurementDao measurementDao = new MeasurementDao(decibel,lux,temperature,humidity,pressure,gass,timestamp);
        Collection<MeasurementDao> measurementDaos = new ArrayList<>();
        measurementDaos.add(measurementDao);

        RoomInformationDao roomInformationDao =
                new RoomInformationDao( noise,  bright,  tempHot,  tempCold,  humLow,
                        humHigh,  presHigh,  presLow,  gas,  sensorDao,  roomDao, measurementDaos);

        Assertions.assertEquals(noise,roomInformationDao.getNoise());
        Assertions.assertEquals(bright,roomInformationDao.getBright());
        Assertions.assertEquals(tempHot,roomInformationDao.getTempHot());
        Assertions.assertEquals(tempCold,roomInformationDao.getTempCold());
        Assertions.assertEquals(humLow,roomInformationDao.getHumLow());
        Assertions.assertEquals(humHigh,roomInformationDao.getHumHigh());
        Assertions.assertEquals(presHigh,roomInformationDao.getPresHigh());
        Assertions.assertEquals(presLow,roomInformationDao.getPresLow());
        Assertions.assertEquals(gas,roomInformationDao.getGas());
        Assertions.assertEquals(9,roomInformationDao.getLimits().size());
        Assertions.assertEquals(roomDao,roomInformationDao.getRoom());
        Assertions.assertEquals(sensorDao,roomInformationDao.getSensor());
        Assertions.assertEquals(1,roomInformationDao.getRoomInfo().size());


        }
}
