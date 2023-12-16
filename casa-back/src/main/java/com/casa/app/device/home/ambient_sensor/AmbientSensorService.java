package com.casa.app.device.home.ambient_sensor;

import com.casa.app.device.large_electric.house_battery.HouseBattery;
import com.casa.app.device.large_electric.house_battery.measurement.HouseBatteryImportExportMeasurement;
import com.casa.app.influxdb.InfluxDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class AmbientSensorService {

    @Autowired
    private AmbientSensorRepository ambientSensorRepository;

    @Autowired
    InfluxDBService influxDBService;

    public void handleMessage(Long id, String message) {
//        System.out.println(message);
        try {
                double temperature = Double.parseDouble(message.split("\\|")[0]);
                double humidity = Double.parseDouble(message.split("\\|")[1]);
//                System.out.println("Temperature: " + Double.toString(temperature));
//                System.out.println("Humidity: " + Double.toString(humidity));
                influxDBService.write(new AmbientSensorMeasurement(id, temperature, humidity, Instant.now()));
        } catch (NumberFormatException e) {
            return;
        }
    }


    public List<AmbientSensorSimulationDTO> getAllSimulation() {
        List<AmbientSensor> ambientSensors = ambientSensorRepository.findAll();
        List<AmbientSensorSimulationDTO> ambientSensorDTOS = new ArrayList<>();
        for (AmbientSensor a : ambientSensors) {
            ambientSensorDTOS.add(new AmbientSensorSimulationDTO(a.getId()));
        }
        return ambientSensorDTOS;
    }
}
