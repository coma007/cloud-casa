package com.casa.app.device.home.air_conditioning;

import com.casa.app.device.home.air_conditioning.dto.AirConditioningSimulationDTO;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/airConditioning")
public class AirConditioningController {

    @Autowired
    private AirConditioningService airConditioningService;

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<AirConditioningSimulationDTO>> getAll() {
        return new ResponseEntity<>(airConditioningService.getAllSimulation(), HttpStatus.OK);
    }
}
