package com.casa.app.device;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.outdoor.lamp.LampMeasurement;
import com.casa.app.device.outdoor.lamp.Lamp;
import com.casa.app.influxdb.InfluxDBConfig;
import com.casa.app.influxdb.InfluxDBService;
import com.influxdb.client.InfluxDBClient;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/device")
public class DeviceController {

    @Autowired
    private DeviceRegistrationService registrationService;

    @PermitAll
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody DeviceRegistrationDTO newDevice){
        registrationService.registerDevice(newDevice);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
