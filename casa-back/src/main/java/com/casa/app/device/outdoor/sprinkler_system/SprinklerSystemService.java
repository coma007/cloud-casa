package com.casa.app.device.outdoor.sprinkler_system;

import com.casa.app.device.DeviceStatus;
import com.casa.app.device.outdoor.lamp.Lamp;
import com.casa.app.device.outdoor.lamp.LampRepository;
import com.casa.app.device.outdoor.lamp.dto.LampSimulationDTO;
import com.casa.app.device.outdoor.sprinkler_system.dto.SprinklerSystemSimulationDTO;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.mqtt.MqttGateway;
import com.casa.app.user.regular_user.RegularUserService;
import com.casa.app.websocket.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<SprinklerSystemSimulationDTO> getAllSimulation() {
        List<SprinklerSystem> sprinklers = sprinklerSystemRepository.findAll();
        List<SprinklerSystemSimulationDTO> sprinklersDTOS = new ArrayList<>();
        for (SprinklerSystem s : sprinklers) {
            sprinklersDTOS.add(new SprinklerSystemSimulationDTO(s.getId(), s.getStatus() == DeviceStatus.ONLINE, false, new SprinklerSystemSchedule(null, null, new boolean[]{false, false, false, false, false, false, false})));
        }
        return sprinklersDTOS;
    }

}
