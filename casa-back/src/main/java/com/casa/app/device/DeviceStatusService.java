package com.casa.app.device;


import com.casa.app.mqtt.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DeviceStatusService {

    @Autowired
    private DeviceRepository deviceRepository;

    //message: name~PING
    public void pingHandler(String message) {
        String[] tokens = message.split("~");
        Device device = deviceRepository.getDeviceByName(tokens[0]);
        device.setStatus(DeviceStatus.ONLINE);
        device.setLastSeen(new Date());
        deviceRepository.save(device);
        System.out.println(message);
    }

    @Scheduled(fixedDelay = 1000*30)
    private void checkStatus() {
        for (Device d : deviceRepository.findAll()) {
            if (d.getStatus() == DeviceStatus.ONLINE) {
                if ((new Date()).getTime() - d.getLastSeen().getTime() > 30*1000) {
                    System.out.println("Pobegulja");
                    d.setStatus(DeviceStatus.OFFLINE);
                    deviceRepository.save(d);
                }
            }
        }
    }

    @Autowired
    private MqttGateway mqttGateway;
    @Scheduled(fixedDelay = 10000)
    private void sendMessage() {
        for (Device d : deviceRepository.findAll()) {
            if (d.getStatus() == DeviceStatus.ONLINE) {
                mqttGateway.sendToMqtt("Server message for " + d.getName(), d.getName());
            }
        }
    }
}