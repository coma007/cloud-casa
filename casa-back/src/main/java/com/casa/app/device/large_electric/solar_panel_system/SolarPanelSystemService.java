package com.casa.app.device.large_electric.solar_panel_system;

import com.casa.app.device.Device;
import com.casa.app.device.DeviceRepository;
import com.casa.app.device.DeviceStatus;
import com.casa.app.device.large_electric.house_battery.HouseBattery;
import com.casa.app.device.large_electric.house_battery.HouseBatteryService;
import com.casa.app.mqtt.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Long.parseLong;

@Service
public class SolarPanelSystemService {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private SolarPanelSystemRepository solarPanelSystemRepository;

    @Autowired
    private MqttGateway mqttGateway;
    @Autowired
    private HouseBatteryService houseBatteryService;

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
    
    public void handleMessage(Long id, String message) {
        // TODO: Save to influx
        Device device = deviceRepository.findById(id).orElse(null);
        double power = Double.parseDouble(message);
        if (device == null || device.getRealEstate() == null) {
            return;
        }
        houseBatteryService.manageEnergy(device, power, true);
    }

    public List<SolarPanelSystemSimulationDTO> getAllSimulation() {
        List<SolarPanelSystem> solarPanelSystems = solarPanelSystemRepository.findAll();
        List<SolarPanelSystemSimulationDTO> solarPanelSystemDTOS = new ArrayList<>();
        for (SolarPanelSystem s : solarPanelSystems) {
            solarPanelSystemDTOS.add(new SolarPanelSystemSimulationDTO(s.getId(), s.getSize(), s.getEfficiency()));
        }
        return solarPanelSystemDTOS;
    }
}
