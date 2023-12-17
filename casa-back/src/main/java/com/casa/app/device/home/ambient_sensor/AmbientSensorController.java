package com.casa.app.device.home.ambient_sensor;

import com.casa.app.device.home.ambient_sensor.dto.AmbientSensorSimulationDTO;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ambientSensor")
public class AmbientSensorController {

    @Autowired
    private AmbientSensorService ambientSensorService;

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<AmbientSensorSimulationDTO>> getAll() {
        return new ResponseEntity<>(ambientSensorService.getAllSimulation(), HttpStatus.OK);
    }
}
