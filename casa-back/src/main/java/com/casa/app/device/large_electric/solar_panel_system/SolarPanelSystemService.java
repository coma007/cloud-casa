package com.casa.app.device.large_electric.solar_panel_system;

import com.casa.app.device.Device;
import com.casa.app.device.DeviceRepository;
import com.casa.app.device.DeviceStatus;
import com.casa.app.mqtt.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static java.lang.Long.parseLong;

@Service
public class SolarPanelSystemService {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private MqttGateway mqttGateway;

    public boolean toggleStatus(Long id) {
        Device device = deviceRepository.findById(id).orElse(null);
        if (device == null) {
            return false;
        }
        if (device.getStatus() == DeviceStatus.OFFLINE) {
            device.setStatus(DeviceStatus.ONLINE);
            mqttGateway.sendToMqtt(device.getId()+"-ON", device.getId().toString());
        } else {
            device.setStatus(DeviceStatus.OFFLINE);
            mqttGateway.sendToMqtt(device.getId()+"-OFF", device.getId().toString());
        }
        deviceRepository.save(device);
        return true;
    }
}
