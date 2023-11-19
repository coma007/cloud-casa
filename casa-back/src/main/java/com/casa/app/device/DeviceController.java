package com.casa.app.device;

import com.casa.app.device.home.air_conditioning.AirConditioningDTO;
import com.casa.app.device.home.ambient_sensor.AmbientSensorDTO;
import com.casa.app.user.UserDTO;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/device")
public class DeviceController {

    @Autowired
    private DeviceCreationService creationService;

    @PermitAll
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody DeviceRegistrationDTO newDevice){
        creationService.createDevice(newDevice);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
