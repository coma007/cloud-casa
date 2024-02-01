package com.casa.app.device.large_electric.electric_vehicle_charger;


import com.casa.app.device.large_electric.electric_vehicle_charger.dto.ElectricVehicleChargerSimulationDTO;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.exceptions.UserNotFoundException;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/electricVehicleCharger")
public class ElectricVehicleChargerController {

    @Autowired
    private ElectricVehicleChargerService electricVehicleChargerService;

    @PermitAll
    @GetMapping("/startCharging/{id}/{slot}")
    public ResponseEntity<?> startCharging(@PathVariable Long id, @PathVariable String slot) throws NotFoundException, UserNotFoundException {
        electricVehicleChargerService.command(id, "Start", slot, "/");
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PermitAll
    @GetMapping("/endCharging/{id}/{slot}")
    public ResponseEntity<?> endCharging(@PathVariable Long id, @PathVariable String slot) throws NotFoundException, UserNotFoundException {
        electricVehicleChargerService.command(id, "End", slot, "/");
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PermitAll
    @GetMapping("/setMaxPercentage/{id}/{slot}/{max}")
    public ResponseEntity<?> setMaxPercentage(@PathVariable Long id, @PathVariable String slot, @PathVariable String max) throws NotFoundException, UserNotFoundException {
        electricVehicleChargerService.command(id, "Set max", slot, max);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<ElectricVehicleChargerSimulationDTO>> getAll() {
        return new ResponseEntity<>(electricVehicleChargerService.getAllSimulation(), HttpStatus.OK);
    }
}
