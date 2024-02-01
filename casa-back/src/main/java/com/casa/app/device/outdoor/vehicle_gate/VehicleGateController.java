package com.casa.app.device.outdoor.vehicle_gate;

import com.casa.app.device.outdoor.vehicle_gate.dto.VehicleGateDTO;
import com.casa.app.device.outdoor.vehicle_gate.dto.VehicleGateDetailsDTO;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.device.outdoor.vehicle_gate.dto.VehicleGateSimulationDTO;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PermitAll
    @GetMapping("/open/{id}")
    public ResponseEntity<?> toggleOn(@PathVariable Long id) throws NotFoundException, UserNotFoundException {
        vehicleGateService.toggle(id, "OPEN");
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("/addLicencePlate/{id}")
    public ResponseEntity<VehicleGateDetailsDTO> addLicencePlate(@PathVariable Long id, @RequestParam String licencePlate) throws NotFoundException, UserNotFoundException {
        VehicleGateDetailsDTO gate = vehicleGateService.addLicencePlate(id, licencePlate);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("/removeLicencePlate/{id}")
    public ResponseEntity<VehicleGateDetailsDTO> removeLicencePlate(@PathVariable Long id, @RequestParam String licencePlate) throws NotFoundException, UserNotFoundException {
        VehicleGateDetailsDTO gate = vehicleGateService.removeLicencePlate(id, licencePlate);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    @PermitAll
    @GetMapping("/mode/{id}")
    public ResponseEntity<?> toggleMode(@PathVariable Long id) throws NotFoundException, UserNotFoundException {
        vehicleGateService.toggle(id, "MODE");
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
