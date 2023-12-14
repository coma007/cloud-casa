package com.casa.app.device.outdoor.vehicle_gate;

import com.casa.app.device.outdoor.lamp.LampBrightnessMeasurement;
import com.casa.app.influxdb.InfluxDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class VehicleGateService {

    @Autowired
    InfluxDBService influxDBService;

    public void licencePlatesHandler(Long id, String message) {
        String licencePlate = message;
        VehicleGateLicencePlatesMeasurement vehicleGate = new VehicleGateLicencePlatesMeasurement( id, licencePlate, Instant.now());
        influxDBService.write(vehicleGate);
    }

    public void commandHandler(Long id, String message) {
        Boolean isOpen = Boolean.parseBoolean(message);
        VehicleGateCommandMeasurement vehicleGate = new VehicleGateCommandMeasurement( id, isOpen, Instant.now());
        influxDBService.write(vehicleGate);
    }

    public void modeHandler(Long id, String message, String user) {
        Boolean isPrivate = Boolean.parseBoolean(message);
        VehicleGateModeMeasurement vehicleGate = new VehicleGateModeMeasurement( id, isPrivate, user, Instant.now());
        influxDBService.write(vehicleGate);
    }
}
