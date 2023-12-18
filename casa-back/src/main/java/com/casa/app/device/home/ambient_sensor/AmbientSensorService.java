package com.casa.app.device.home.ambient_sensor;

import com.casa.app.device.home.ambient_sensor.dto.AmbientSensorSimulationDTO;
import com.casa.app.device.large_electric.house_battery.measurement.HouseBatteryPowerUsageMeasurement;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.websocket.SocketMessage;
import com.casa.app.websocket.WebSocketController;
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
    @Autowired
    private WebSocketController webSocketController;

    public void handleMessage(Long id, String message) {
//        System.out.println(message);
        try {
                double temperature = Double.parseDouble(message.split("\\|")[0]);
                double humidity = Double.parseDouble(message.split("\\|")[1]);
//                System.out.println("Temperature: " + Double.toString(temperature));
//                System.out.println("Humidity: " + Double.toString(humidity));
                influxDBService.write(new AmbientSensorMeasurement(id, temperature, humidity, Instant.now()));
            AmbientSensorMeasurement measurement = new AmbientSensorMeasurement(id, temperature, humidity, Instant.now());
            webSocketController.sendMessage(new SocketMessage<AmbientSensorMeasurement>("ambient_sensor_reading", "New value", null, id.toString(), measurement));
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
