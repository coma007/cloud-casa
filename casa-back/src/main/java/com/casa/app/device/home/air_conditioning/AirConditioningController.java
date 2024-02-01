package com.casa.app.device.home.air_conditioning;

import com.casa.app.device.home.air_conditioning.dtos.AirConditionModeDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionScheduleDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionTemperatureDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionWorkingDTO;
import com.casa.app.device.home.air_conditioning.schedule.AirConditionSchedule;
import com.casa.app.exceptions.DeviceNotFoundException;
import com.casa.app.exceptions.InvalidDateException;
import com.casa.app.exceptions.ScheduleOverlappingException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.permission.PermissionService;
import com.casa.app.user.User;
import com.casa.app.user.UserService;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserService;
import com.casa.app.device.home.air_conditioning.dto.AirConditioningSimulationDTO;
import com.casa.app.util.email.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airConditioning")
public class AirConditioningController {

    @Autowired
    private AirConditioningService airConditioningService;

    @Autowired
    UserService userService;

    @Autowired
    PermissionService permissionService;

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<AirConditioningSimulationDTO>> getAll() {
        return new ResponseEntity<>(airConditioningService.getAllSimulation(), HttpStatus.OK);
    }

    @PermitAll
    @PostMapping("/simulation/working")
    public ResponseEntity<?> setWorking(@RequestBody AirConditionWorkingDTO dto) throws UserNotFoundException, DeviceNotFoundException {
        User currentUser = userService.getUserByToken();
        if(!permissionService.canWriteDevice(dto.getId(), currentUser.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot access, no write permission");

        airConditioningService.sendWorkingCommand(dto, currentUser);
        return ResponseEntity.ok().build();
    }

    @PermitAll
    @GetMapping(value="/public/simulation/schedules",
            produces="application/json")
    public ResponseEntity<String> getSchedules(@RequestParam Long deviceId) {
        User currentUser = userService.getUserByToken();
        if(!permissionService.canReadDevice(deviceId, currentUser.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot access, no read permission");

        List<AirConditionSchedule> schedules = airConditioningService.getSchedules(deviceId);
        try {
            return ResponseEntity.ok(JSONUtil.getJsonWithDate(schedules));
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body("Error parsing JSON");
        }
    }

    @PermitAll
    @PostMapping("/simulation/temperature")
    public ResponseEntity<?> setTemperature(@RequestBody AirConditionTemperatureDTO dto) throws UserNotFoundException, DeviceNotFoundException {
        User currentUser = userService.getUserByToken();
        if(!permissionService.canWriteDevice(dto.getId(), currentUser.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot access, no write permission");

        airConditioningService.sendTemperatureCommand(dto, currentUser);
        return ResponseEntity.ok().build();
    }

    @PermitAll
    @PostMapping("/simulation/mode")
    public ResponseEntity<?> setMode(@RequestBody AirConditionModeDTO dto) throws UserNotFoundException, DeviceNotFoundException {
        User currentUser = userService.getUserByToken();
        if(!permissionService.canWriteDevice(dto.getId(), currentUser.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot access, no write permission");

        if(dto.getMode().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mode field must be filled");
        }
        airConditioningService.sendModeCommand(dto, currentUser);
        return ResponseEntity.ok().build();
    }

    @PermitAll
    @PostMapping("/simulation/schedule")
    public ResponseEntity<?> setSchedule(@RequestBody AirConditionScheduleDTO dto) throws DeviceNotFoundException, InvalidDateException, ScheduleOverlappingException, UserNotFoundException {
        User currentUser = userService.getUserByToken();
        if(!permissionService.canWriteDevice(dto.getDeviceId(), currentUser.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot access, no write permission");

        if(dto.isRepeating() && dto.getRepeatingDaysIncrement() == null){
            return ResponseEntity.badRequest().body("Repeat is set but increment is not, try setting increment");
        }
        if(dto.getRepeatingDaysIncrement() <= 0){
            return ResponseEntity.badRequest().body("Increment must be whole number greater than 0");
        }
        airConditioningService.setSchedule(dto, currentUser);
        return ResponseEntity.ok().build();
    }
}
