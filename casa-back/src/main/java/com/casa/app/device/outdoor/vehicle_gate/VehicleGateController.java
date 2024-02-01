package com.casa.app.device.outdoor.vehicle_gate;

import com.casa.app.exceptions.NotFoundException;
import com.casa.app.exceptions.UnauthorizedWriteException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.device.outdoor.vehicle_gate.dto.VehicleGateSimulationDTO;
import com.casa.app.permission.PermissionService;
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
@RequestMapping("/api/vehicleGate")
public class VehicleGateController {

    @Autowired
    private VehicleGateService vehicleGateService;
    @Autowired
    private PermissionService permissionService;

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<VehicleGateSimulationDTO>> getAll() {
        return new ResponseEntity<>(vehicleGateService.getAllSimulation(), HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("/open/{id}")
    public ResponseEntity<?> toggleOn(@PathVariable Long id) throws NotFoundException, UserNotFoundException, UnauthorizedWriteException {
        permissionService.canWrite(id);
        vehicleGateService.toggle(id, "OPEN");
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("/mode/{id}")
    public ResponseEntity<?> toggleMode(@PathVariable Long id) throws NotFoundException, UserNotFoundException, UnauthorizedWriteException {
        permissionService.canWrite(id);
        vehicleGateService.toggle(id, "MODE");
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
