package com.casa.app.device.outdoor.lamp;


import com.casa.app.exceptions.NotFoundException;
import com.casa.app.exceptions.UnauthorizedWriteException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.device.outdoor.lamp.dto.LampSimulationDTO;
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
@RequestMapping("/api/lamp")
public class LampController {

    @Autowired
    private LampService lampService;
    @Autowired
    private PermissionService permissionService;

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<LampSimulationDTO>> getAll() {
        return new ResponseEntity<>(lampService.getAllSimulation(), HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("/turnOn/{id}")
    public ResponseEntity<?> toggleOn(@PathVariable Long id) throws NotFoundException, UserNotFoundException, UnauthorizedWriteException {
        permissionService.canWrite(id);
        lampService.toggleOn(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
