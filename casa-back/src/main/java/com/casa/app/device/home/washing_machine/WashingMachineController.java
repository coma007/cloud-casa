package com.casa.app.device.home.washing_machine;

import com.casa.app.device.home.washing_machine.dto.WashingMachineModeDTO;
import com.casa.app.device.home.washing_machine.dto.WashingMachineScheduleDTO;
import com.casa.app.device.home.washing_machine.dto.WashingMachineSimulationDTO;
import com.casa.app.device.home.washing_machine.dto.WashingMachineWorkingDTO;
import com.casa.app.device.home.washing_machine.schedule.WashingMachineSchedule;
import com.casa.app.exceptions.*;
import com.casa.app.permission.PermissionService;
import com.casa.app.user.User;
import com.casa.app.user.UserService;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserService;
import com.casa.app.util.email.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/washingMachine")
public class WashingMachineController {

    @Autowired
    private RegularUserService regularUserService;
    @Autowired
    private WashingMachineService washingMachineService;
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<WashingMachineSimulationDTO>> getAll() {
        return new ResponseEntity<>(washingMachineService.getAllSimulation(), HttpStatus.OK);
    }

    @PermitAll
    @PostMapping("/simulation/mode")
    public ResponseEntity<?> setMode(@RequestBody WashingMachineModeDTO dto) throws UserNotFoundException, DeviceNotFoundException, UnauthorizedWriteException {
        if(dto.getMode().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mode field must be filled");
        }
        permissionService.canWrite(dto.getId());
        User user = userService.getUserByToken();
        washingMachineService.sendModeCommand(dto, user);
        return ResponseEntity.ok().build();
    }

    @PermitAll
    @PostMapping("/simulation/working")
    public ResponseEntity<?> setWorking(@RequestBody WashingMachineWorkingDTO dto) throws UserNotFoundException, DeviceNotFoundException, UnauthorizedWriteException {
        User user = userService.getUserByToken();
        permissionService.canWrite(dto.getId());
        washingMachineService.sendWorkingCommand(dto, user);
        return ResponseEntity.ok().build();
    }

    @PermitAll
    @PostMapping("/simulation/schedule")
    public ResponseEntity<?> setSchedule(@RequestBody WashingMachineScheduleDTO dto) throws DeviceNotFoundException, InvalidDateException, ScheduleOverlappingException, UserNotFoundException, UnauthorizedWriteException {
        User user = userService.getUserByToken();
        permissionService.canWrite(dto.getDeviceId());
        washingMachineService.setSchedule(dto, user);
        return ResponseEntity.ok().build();
    }

    @PermitAll
    @GetMapping(value="/public/simulation/schedules",
            produces="application/json")
    public ResponseEntity<String> getSchedules(@RequestParam Long deviceId) {
        List<WashingMachineSchedule> schedules = washingMachineService.getSchedules(deviceId);
        try {
            return ResponseEntity.ok(JSONUtil.getJsonWithDate(schedules));
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body("Error parsing JSON");
        }
    }
}
