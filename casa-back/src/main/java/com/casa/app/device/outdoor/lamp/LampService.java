package com.casa.app.device.outdoor.lamp;

import com.casa.app.device.DeviceStatus;
import com.casa.app.device.measurement.MeasurementType;
import com.casa.app.device.outdoor.lamp.dto.LampSimulationDTO;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.websocket.SocketMessage;
import com.casa.app.websocket.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class LampService {

    @Autowired
    private LampRepository lampRepository;
    @Autowired
    InfluxDBService influxDBService;

    @Autowired
    private WebSocketController webSocketController;

    public void brightnessHandler(Long id, String message) {
        Double brightness = Double.parseDouble(message);
        LampBrightnessMeasurement lamp = new LampBrightnessMeasurement( id, brightness, Instant.now());
        webSocketController.sendMessage(new SocketMessage<>(MeasurementType.lampBrightness, message, id.toString(), id.toString(), null));
        influxDBService.write(lamp);
    }

    public void commandHandler(Long id, String message, String user) {
        Boolean isOn = Boolean.parseBoolean(message);
        LampCommandMeasurement lamp = new LampCommandMeasurement( id, isOn, user, Instant.now());
        webSocketController.sendMessage(new SocketMessage<>(MeasurementType.lampCommand, message, id.toString(), id.toString(), null));
        influxDBService.write(lamp);
    }

    public List<LampSimulationDTO> getAllSimulation() {
        List<Lamp> lamps = lampRepository.findAll();
        List<LampSimulationDTO> lampDTOS = new ArrayList<>();
        for (Lamp l : lamps) {
            lampDTOS.add(new LampSimulationDTO(l.getId(), l.getStatus() == DeviceStatus.ONLINE));
        }
        return lampDTOS;
    }
}
