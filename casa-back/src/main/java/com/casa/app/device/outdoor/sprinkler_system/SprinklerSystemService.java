package com.casa.app.device.outdoor.sprinkler_system;

import com.casa.app.device.DeviceStatus;
import com.casa.app.device.measurement.MeasurementType;
import com.casa.app.device.outdoor.sprinkler_system.dto.SprinklerSystemSimulationDTO;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.mqtt.MqttGateway;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserService;
import com.casa.app.websocket.SocketMessage;
import com.casa.app.websocket.WebSocketController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SprinklerSystemService {
    @Autowired
    private SprinklerSystemRepository sprinklerSystemRepository;
    @Autowired
    InfluxDBService influxDBService;

    @Autowired
    private WebSocketController webSocketController;
    @Autowired
    private MqttGateway mqttGateway;
    @Autowired
    private RegularUserService regularUserService;

    public void commandHandler(Long id, String message) {
        String user = message.split("|")[1];
        Boolean isOn = Boolean.parseBoolean(message.split("|")[0]);
        SprinklerSystemCommandMeasurement sprinkler = new SprinklerSystemCommandMeasurement( id, isOn, user, Instant.now());
        if (user.equals("SIMULATION")) {
            webSocketController.sendMessage(new SocketMessage<>(MeasurementType.sprinklerCommand, message, id.toString(), id.toString(), sprinkler));
        }
        influxDBService.write(sprinkler);
    }

    public void scheduleHandler(Long id, String message) {
        String user = message;
        SprinklerSystemScheduleMeasurement sprinkler = new SprinklerSystemScheduleMeasurement( id, user, Instant.now());
        influxDBService.write(sprinkler);
    }

    public void toggleOn(Long id) throws NotFoundException, UserNotFoundException {
        RegularUser currentUser = regularUserService.getUserByToken();
        Optional<SprinklerSystem> sprinklerSystem = sprinklerSystemRepository.findById(id);
        if (sprinklerSystem.isEmpty()) {
            throw new NotFoundException();
        }
        mqttGateway.sendToMqtt(id + "~ON~" + currentUser.getUsername(), id.toString());
    }

    public void setSchedule(Long id, SprinklerSystemSchedule schedule) throws NotFoundException, UserNotFoundException {
        RegularUser currentUser = regularUserService.getUserByToken();
        Optional<SprinklerSystem> sprinklerSystem = sprinklerSystemRepository.findById(id);
        if (sprinklerSystem.isEmpty()) {
            throw new NotFoundException();
        }
        String scheduleJson = getSprinklerJson(schedule);
        sprinklerSystem.get().setSchedule(schedule);
        sprinklerSystemRepository.save(sprinklerSystem.get());
        mqttGateway.sendToMqtt(id + "~SCHEDULE~" + currentUser.getUsername() + "|" + scheduleJson, id.toString());
    }

    public List<SprinklerSystemSimulationDTO> getAllSimulation() {
        List<SprinklerSystem> sprinklers = sprinklerSystemRepository.findAll();
        List<SprinklerSystemSimulationDTO> sprinklersDTOS = new ArrayList<>();
        for (SprinklerSystem s : sprinklers) {
            sprinklersDTOS.add(new SprinklerSystemSimulationDTO(s.getId(), s.getStatus() == DeviceStatus.ONLINE, false, false, s.getSchedule()));
        }
        return sprinklersDTOS;
    }

    private static String getSprinklerJson(SprinklerSystemSchedule schedule) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES);
        String scheduleJson;
        try {
            scheduleJson = objectMapper.writeValueAsString(schedule);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting SprinklerSystemSchedule to JSON", e);
        }
        return scheduleJson;
    }


}
