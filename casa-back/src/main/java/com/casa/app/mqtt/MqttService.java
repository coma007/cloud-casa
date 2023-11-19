package com.casa.app.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    @Autowired
    private MqttGateway mqttGateway;

    @Scheduled(fixedDelay = 10000)
    private void sendMessage() {
        mqttGateway.sendToMqtt("Server message", "serverMessages");
    }
}
