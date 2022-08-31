package at.qe.skeleton.tests;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.ui.beans.AverageCalculatorReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@WebAppConfiguration
public class AverageCalculatorReportTest {

    @Autowired
    MeasurementService measurementService;

    @Autowired
    AverageCalculatorReport averageCalculatorReport;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoGetAverageCalculatedAllMeasurements() {
        List<Measurement> measurementList =new ArrayList<>(measurementService.getAllMeasurements());

        double temperature = measurementList.stream()
                .filter(type -> type.getType().equals(MeasurementType.TEMPERATURE))
                .mapToDouble(Measurement::getValue)
                .average()
                .orElse(0.0);
        double humidity = measurementList.stream()
                .filter(type -> type.getType().equals(MeasurementType.HUMIDITY))
                .mapToDouble(Measurement::getValue)
                .average()
                .orElse(0.0);
        double lux = measurementList.stream()
                .filter(type -> type.getType().equals(MeasurementType.LUX))
                .mapToDouble(Measurement::getValue)
                .average()
                .orElse(0.0);
        double gas = measurementList.stream()
                .filter(type -> type.getType().equals(MeasurementType.GAS))
                .mapToDouble(Measurement::getValue)
                .average()
                .orElse(0.0);
        double decibel = measurementList.stream()
                .filter(type -> type.getType().equals(MeasurementType.DECIBEL))
                .mapToDouble(Measurement::getValue)
                .average()
                .orElse(0.0);
        double pressure = measurementList.stream()
                .filter(type -> type.getType().equals(MeasurementType.PRESSURE))
                .mapToDouble(Measurement::getValue)
                .average()
                .orElse(0.0);

        BigDecimal bdTemp = BigDecimal.valueOf(temperature).setScale(2, RoundingMode.HALF_UP);
        BigDecimal bdHum = BigDecimal.valueOf(humidity).setScale(2, RoundingMode.HALF_UP);
        BigDecimal bdLux = BigDecimal.valueOf(lux).setScale(2, RoundingMode.HALF_UP);
        BigDecimal bdGas = BigDecimal.valueOf(gas).setScale(2, RoundingMode.HALF_UP);
        BigDecimal bdDec = BigDecimal.valueOf(decibel).setScale(2, RoundingMode.HALF_UP);
        BigDecimal bdPres = BigDecimal.valueOf(pressure).setScale(2, RoundingMode.HALF_UP);


        averageCalculatorReport.calculateAverages(measurementList);
        Assertions.assertEquals(bdTemp.doubleValue(),averageCalculatorReport.getTemperature());
        Assertions.assertEquals(bdHum.doubleValue(),averageCalculatorReport.getHumidity());
        Assertions.assertEquals(bdLux.doubleValue(),averageCalculatorReport.getLux());
        Assertions.assertEquals(bdGas.doubleValue(),averageCalculatorReport.getGas());
        Assertions.assertEquals(bdDec.doubleValue(),averageCalculatorReport.getDecibel());
        Assertions.assertEquals(bdPres.doubleValue(),averageCalculatorReport.getPressure());


    }
}
