package com.casa.app.device.large_electric.house_battery;

import com.casa.app.device.large_electric.house_battery.dto.HouseBatterySimulationDTO;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/houseBattery")
public class HouseBatteryController {

    @Autowired
    private HouseBatteryService houseBatteryService;

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<HouseBatterySimulationDTO>> getAll() {
        return new ResponseEntity<>(houseBatteryService.getAllSimulation(), HttpStatus.OK);
    }

    
}
