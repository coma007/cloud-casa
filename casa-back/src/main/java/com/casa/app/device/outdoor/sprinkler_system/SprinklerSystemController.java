package com.casa.app.device.outdoor.sprinkler_system;

import com.casa.app.device.outdoor.lamp.LampService;
import com.casa.app.device.outdoor.lamp.dto.LampSimulationDTO;
import com.casa.app.device.outdoor.sprinkler_system.dto.SprinklerSystemSimulationDTO;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.exceptions.UserNotFoundException;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sprinklerSystem")
public class SprinklerSystemController {

    @Autowired
    private SprinklerSystemService sprinklerSystemService;

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<SprinklerSystemSimulationDTO>> getAll() {
        return new ResponseEntity<>(sprinklerSystemService.getAllSimulation(), HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("/turnOn/{id}")
    public ResponseEntity<?> toggleOn(@PathVariable Long id) throws NotFoundException, UserNotFoundException {
        sprinklerSystemService.toggleOn(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("/setSchedule/{id}")
    public ResponseEntity<?> toggleOn(@PathVariable Long id, @RequestBody SprinklerSystemSchedule schedule) throws NotFoundException, UserNotFoundException {
        sprinklerSystemService.setSchedule(id, schedule);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
