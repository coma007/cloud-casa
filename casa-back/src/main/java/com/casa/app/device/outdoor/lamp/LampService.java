package com.casa.app.device.outdoor.lamp;

import com.casa.app.device.DeviceStatus;
import com.casa.app.device.measurement.MeasurementType;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.mqtt.MqttGateway;
import com.casa.app.user.User;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserService;
import com.casa.app.websocket.SocketMessage;
import com.casa.app.websocket.WebSocketController;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LampService {

    @Autowired
    private LampRepository lampRepository;
    @Autowired
    InfluxDBService influxDBService;

    @Autowired
    private WebSocketController webSocketController;
    @Autowired
    private MqttGateway mqttGateway;
    @Autowired
    private RegularUserService regularUserService;

    public void brightnessHandler(Long id, String message) {
        Double brightness = Double.parseDouble(message);
        LampBrightnessMeasurement lamp = new LampBrightnessMeasurement( id, brightness, Instant.now());
        webSocketController.sendMessage(new SocketMessage<>(MeasurementType.lampBrightness, message, id.toString(), id.toString(), null));
        influxDBService.write(lamp);
    }

    public void commandHandler(Long id, String message) {
        String user = message.split("|")[1];
        Boolean isOn = Boolean.parseBoolean(message.split("|")[0]);
        LampCommandMeasurement lamp = new LampCommandMeasurement( id, isOn, user, Instant.now());
        if (user.equals("SIMULATION")) {
            webSocketController.sendMessage(new SocketMessage<>(MeasurementType.lampCommand, message, id.toString(), id.toString(), null));
        }
        influxDBService.write(lamp);
    }

    public void toggleOn(Long id) throws NotFoundException, UserNotFoundException {
        RegularUser currentUser = regularUserService.getUserByToken();
        Optional<Lamp> lamp = lampRepository.findById(id);
        if (lamp.isEmpty()) {
            throw new NotFoundException();
        }
        mqttGateway.sendToMqtt(id + "~ON~" + currentUser.getUsername(), id.toString());
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
