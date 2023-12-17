package com.casa.app.device;


import com.casa.app.device.large_electric.house_battery.HouseBattery;
import com.casa.app.device.large_electric.house_battery.HouseBatteryService;
import com.casa.app.mqtt.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Long.parseLong;

@Service
public class DeviceStatusService {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private HouseBatteryService houseBatteryService;

    //message: id~PING
    public void pingHandler(Long id) {
        Device device = deviceRepository.findById(id).orElse(null);
        if (device == null) {
            return;
        }
        device.setStatus(DeviceStatus.ONLINE);
        device.setLastSeen(new Date());
        deviceRepository.save(device);
        System.out.println(id + " PING");
        if (device.getRealEstate() == null) {
            return;
        }
        if (device.getPowerSupplyType() == PowerSupplyType.HOME) {
            houseBatteryService.manageEnergy(device, device.getEnergyConsumption(), false);
        }
    }

    @Scheduled(fixedDelay = 1000*30)
    private void checkStatus() {
        for (Device d : deviceRepository.findAll()) {
            if (d.getStatus() == DeviceStatus.ONLINE) {
                if ((new Date()).getTime() - d.getLastSeen().getTime() > 2*60*1000) {
//                    System.out.println("Pobegulja");
                    d.setStatus(DeviceStatus.OFFLINE);
                    deviceRepository.save(d);
                }
            }
        }
    }

//    @Autowired
//    private MqttGateway mqttGateway;
//    @Scheduled(fixedDelay = 5*60*1000)
//    private void sendMessage() {
//        for (Device d : deviceRepository.findAll()) {
//            if (d.getStatus() == DeviceStatus.ONLINE) {
//                mqttGateway.sendToMqtt(d.getName()+"-OFF", d.getName());
//            }
//        }
//    }
}
