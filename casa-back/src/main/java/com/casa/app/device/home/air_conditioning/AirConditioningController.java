package com.casa.app.device.home.air_conditioning;

import com.casa.app.device.home.air_conditioning.dtos.AirConditionModeDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionTemperatureDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionWorkingDTO;
import com.casa.app.exceptions.UserNotFoundException;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PermitAll
    @PostMapping("/simulation/working")
    public ResponseEntity<Boolean> setWorking(@RequestBody AirConditionWorkingDTO dto) throws UserNotFoundException {
        airConditioningService.sendWorkingCommand(dto);
        return ResponseEntity.ok().build();
    }

    @PermitAll
    @PostMapping("/simulation/temperature")
    public ResponseEntity<Boolean> setTemperature(@RequestBody AirConditionTemperatureDTO dto) throws UserNotFoundException {
        airConditioningService.sendTemperatureCommand(dto);
        return ResponseEntity.ok().build();
    }

    @PermitAll
    @PostMapping("/simulation/mode")
    public ResponseEntity<Boolean> setMode(@RequestBody AirConditionModeDTO dto) throws UserNotFoundException {
        airConditioningService.sendModeCommand(dto);
        return ResponseEntity.ok().build();
    }
}
