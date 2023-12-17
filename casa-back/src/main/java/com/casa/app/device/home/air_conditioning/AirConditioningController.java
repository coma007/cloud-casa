package com.casa.app.device.home.air_conditioning;

import com.casa.app.device.home.air_conditioning.dtos.AirConditionModeDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionScheduleDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionTemperatureDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionWorkingDTO;
import com.casa.app.exceptions.DeviceNotFoundException;
import com.casa.app.exceptions.InvalidDateException;
import com.casa.app.exceptions.ScheduleOverlappingException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserService;
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

    @Autowired
    RegularUserService regularUserService;

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<AirConditioningSimulationDTO>> getAll() {
        return new ResponseEntity<>(airConditioningService.getAllSimulation(), HttpStatus.OK);
    }

    @PermitAll
    @PostMapping("/simulation/working")
    public ResponseEntity<Boolean> setWorking(@RequestBody AirConditionWorkingDTO dto) throws UserNotFoundException, DeviceNotFoundException {
        RegularUser currentUser = regularUserService.getUserByToken();
        airConditioningService.sendWorkingCommand(dto, currentUser);
        return ResponseEntity.ok().build();
    }

    @PermitAll
    @PostMapping("/simulation/temperature")
    public ResponseEntity<Boolean> setTemperature(@RequestBody AirConditionTemperatureDTO dto) throws UserNotFoundException, DeviceNotFoundException {
        RegularUser currentUser = regularUserService.getUserByToken();
        airConditioningService.sendTemperatureCommand(dto, currentUser);
        return ResponseEntity.ok().build();
    }

    @PermitAll
    @PostMapping("/simulation/mode")
    public ResponseEntity<Boolean> setMode(@RequestBody AirConditionModeDTO dto) throws UserNotFoundException, DeviceNotFoundException {
        RegularUser currentUser = regularUserService.getUserByToken();
        airConditioningService.sendModeCommand(dto, currentUser);
        return ResponseEntity.ok().build();
    }

    @PermitAll
    @PostMapping("/simulation/schedule")
    public ResponseEntity<Boolean> setSchedule(@RequestBody AirConditionScheduleDTO dto) throws DeviceNotFoundException, InvalidDateException, ScheduleOverlappingException {
        airConditioningService.setSchedule(dto);
        return ResponseEntity.ok().build();
    }
}
