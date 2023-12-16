package com.casa.app.device.outdoor.vehicle_gate;

import com.casa.app.device.outdoor.vehicle_gate.dto.VehicleGateSimulationDTO;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vehicleGate")
public class VehicleGateController {

    @Autowired
    private VehicleGateService vehicleGateService;
    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<VehicleGateSimulationDTO>> getAll() {
        return new ResponseEntity<>(vehicleGateService.getAllSimulation(), HttpStatus.OK);
    }
}
