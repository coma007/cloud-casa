package com.casa.app.device.large_electric.solar_panel_system;

import com.casa.app.device.Device;
import com.casa.app.device.DeviceRepository;
import com.casa.app.device.DeviceStatus;
import com.casa.app.device.large_electric.house_battery.HouseBatteryService;
import com.casa.app.device.large_electric.solar_panel_system.dto.SolarPanelSystemSimulationDTO;
import com.casa.app.device.large_electric.solar_panel_system.measurement.SolarPanelSystemCommand;
import com.casa.app.device.large_electric.solar_panel_system.measurement.SolarPanelSystemPowerMeasurement;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.mqtt.MqttGateway;
import com.casa.app.user.User;
import com.casa.app.user.UserService;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class SolarPanelSystemService {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private SolarPanelSystemRepository solarPanelSystemRepository;
    @Autowired
    private HouseBatteryService houseBatteryService;
    @Autowired
    private UserService userService;

    @Autowired
    private MqttGateway mqttGateway;
    @Autowired
    private InfluxDBService influxDBService;
    @Autowired
    private RegularUserService regularUserService;

    public DeviceStatus toggleStatus(Long id) throws UserNotFoundException {
        Device device = deviceRepository.findById(id).orElse(null);
        RegularUser currentUser = regularUserService.getUserByToken();
        if (device == null) {
            return null;
        }
        if (device.getStatus() == DeviceStatus.OFFLINE) {
            device.setStatus(DeviceStatus.ONLINE);
            SolarPanelSystemCommand command = new SolarPanelSystemCommand(device.getId(), "TURN ON", currentUser.getUsername(), Instant.now());
            influxDBService.write(command);
            mqttGateway.sendToMqtt(device.getId()+"~ON", device.getId().toString());
        } else {
            device.setStatus(DeviceStatus.OFFLINE);
            SolarPanelSystemCommand command = new SolarPanelSystemCommand(device.getId(), "TURN OFF", currentUser.getUsername(), Instant.now());
            influxDBService.write(command);
            mqttGateway.sendToMqtt(device.getId()+"~OFF", device.getId().toString());
        }
        deviceRepository.save(device);
        return device.getStatus();
    }
    
    public void handleMessage(Long id, String powerString) {
        Device device = deviceRepository.findById(id).orElse(null);
        double power = Double.parseDouble(powerString);
        if (device == null || device.getRealEstate() == null) {
            return;
        }
        SolarPanelSystemPowerMeasurement panel = new SolarPanelSystemPowerMeasurement(device.getId(), power, Instant.now());
        influxDBService.write(panel);
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
