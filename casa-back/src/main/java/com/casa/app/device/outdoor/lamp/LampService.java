package com.casa.app.device.outdoor.lamp;

import com.casa.app.influxdb.InfluxDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LampService {

    @Autowired
    InfluxDBService influxDBService;

    public void brightnessHandler(Long id, String message) {
        Double brightness = Double.parseDouble(message);
        LampBrightnessMeasurement lamp = new LampBrightnessMeasurement( id, brightness, Instant.now());
        influxDBService.write(lamp);
    }

    public void commandHandler(Long id, String message, String user) {
        Boolean isOn = Boolean.parseBoolean(message);
        LampCommandMeasurement lamp = new LampCommandMeasurement( id, isOn, user, Instant.now());
        influxDBService.write(lamp);
    }

}
