package com.casa.app.device.large_electric.electric_vehicle_charger;


import com.casa.app.device.large_electric.electric_vehicle_charger.dto.ElectricVehicleChargerSimulationDTO;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/electricVehicleCharger")
public class ElectricVehicleChargerController {

    @Autowired
    private ElectricVehicleChargerService electricVehicleChargerService;

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<ElectricVehicleChargerSimulationDTO>> getAll() {
        return new ResponseEntity<>(electricVehicleChargerService.getAllSimulation(), HttpStatus.OK);
    }
}
