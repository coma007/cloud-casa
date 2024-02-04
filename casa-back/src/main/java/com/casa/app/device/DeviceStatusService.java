package com.casa.app.device;


import com.casa.app.device.large_electric.house_battery.HouseBattery;
import com.casa.app.device.large_electric.house_battery.HouseBatteryService;
import com.casa.app.device.measurement.MeasurementType;
import com.casa.app.device.measurement.OnlineMeasurement;
import com.casa.app.device.outdoor.lamp.LampBrightnessMeasurement;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.mqtt.MqttGateway;
import com.casa.app.websocket.SocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    @Autowired
    InfluxDBService influxDBService;

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

        OnlineMeasurement online = new OnlineMeasurement( id, true, Instant.now());
        influxDBService.write(online);
    }

    @Scheduled(fixedDelay = 1000*30)
    private void checkStatus() {
        for (Device d : deviceRepository.findAll()) {
            if (d.getStatus() == DeviceStatus.ONLINE) {
                if (d.getLastSeen() != null && (new Date()).getTime() - d.getLastSeen().getTime() > 2*60*1000) {
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
