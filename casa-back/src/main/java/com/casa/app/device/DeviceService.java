package com.casa.app.device;

import com.casa.app.device.dto.DeviceSimulationDTO;
import com.casa.app.device.home.air_conditioning.AirConditioning;
import com.casa.app.device.home.ambient_sensor.AmbientSensor;
import com.casa.app.device.home.washing_machine.WashingMachine;
import com.casa.app.device.large_electric.electric_vehicle_charger.ElectricVehicleCharger;
import com.casa.app.device.large_electric.house_battery.HouseBattery;
import com.casa.app.device.outdoor.lamp.Lamp;
import com.casa.app.device.outdoor.sprinkler_system.SprinklerSystem;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    public List<DeviceSimulationDTO> getAllSimulation() {
        List<Device> devices = deviceRepository.findAll();
        List<DeviceSimulationDTO> devicesDTO = new ArrayList<>();
        for (Device d : devices) {
            String type = getType(d);
            devicesDTO.add(new DeviceSimulationDTO(d.getName(), type));
        }
        return devicesDTO;
    }

    private static String getType(Device d) {
        String type = "SolarPanelSystem";
        if (d instanceof AmbientSensor) {
            type = "AmbientSensor";
        } else if (d instanceof AirConditioning) {
            type = "AirConditioning";
        } else if (d instanceof WashingMachine) {
            type = "WashingMachine";
        } else if (d instanceof Lamp) {
            type = "Lamp";
        } else if (d instanceof SprinklerSystem) {
            type = "SprinklerSystem";
        } else if (d instanceof VehicleGate) {
            type = "VehicleGate";
        } else if (d instanceof ElectricVehicleCharger) {
            type = "ElectricVehicleCharger";
        } else if (d instanceof HouseBattery) {
            type = "HouseBattery";
        }
        return type;
    }
}
