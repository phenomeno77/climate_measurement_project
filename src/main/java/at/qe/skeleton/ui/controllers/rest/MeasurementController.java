package at.qe.skeleton.ui.controllers.rest;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.services.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/measurement")
public class MeasurementController {

    @Autowired
    private MeasurementService measurementService;

    @PostMapping("/save")
    Measurement addMeasurement(@RequestBody Measurement measurement){
        return measurementService.saveMeasurement(measurement);
    }
}
