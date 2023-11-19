package com.casa.app.device;

import com.casa.app.device.home.air_conditioning.AirConditioning;
import com.casa.app.device.home.air_conditioning.AirConditioningDTO;
import com.casa.app.device.home.air_conditioning.AirConditioningRepository;
import com.casa.app.device.home.ambient_sensor.AmbientSensor;
import com.casa.app.device.home.ambient_sensor.AmbientSensorDTO;
import com.casa.app.device.home.ambient_sensor.AmbientSensorRepository;
import com.casa.app.device.home.washing_machine.WashingMachine;
import com.casa.app.device.home.washing_machine.WashingMachineDTO;
import com.casa.app.device.large_electric.electric_vehicle_charger.ElectricVehicleChargerDTO;
import com.casa.app.device.large_electric.house_battery.HouseBatteryDTO;
import com.casa.app.device.large_electric.solar_panel_system.SolarPanelSystemDTO;
import com.casa.app.device.outdoor.lamp.LampDTO;
import com.casa.app.device.outdoor.sprinkler_system.SprinklerSystemDTO;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGateDTO;
import com.casa.app.estate.RealEstateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceCreationService {

    @Autowired
    private RealEstateService realEstateService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private AmbientSensorRepository ambientSensorRepository;
    @Autowired
    private AirConditioningRepository airConditioningRepository;

    public void createDevice(DeviceRegistrationDTO deviceDTO) {
        String type = deviceDTO.getType();
        Object device = deviceDTO.getDevice();
        ObjectMapper mapper = new ObjectMapper();
        // Determine the type of device and handle creation accordingly
        switch (type) {
            case "AmbientSensor":
                createAmbientSensor(mapper.convertValue(device, AmbientSensorDTO.class));
                break;
            case "AirConditioning":
                createAirConditioning(mapper.convertValue(device, AirConditioningDTO.class));
                break;
            case "WashingMachine":
                createWashingMachine(mapper.convertValue(device, WashingMachineDTO.class));
                break;
            case "Lamp":
                createLamp(mapper.convertValue(device, LampDTO.class));
                break;
            case "SprinklerSystem":
                createSprinklerSystem(mapper.convertValue(device, SprinklerSystemDTO.class));
                break;
            case "VehicleGate":
                createVehicleGate(mapper.convertValue(device, VehicleGateDTO.class));
                break;
            case "SolarPanelSystem":
                createSolarPanelSystem(mapper.convertValue(device, SolarPanelSystemDTO.class));
                break;
            case "HouseBattery":
                createHouseBattery(mapper.convertValue(device, HouseBatteryDTO.class));
                break;
            case "ElectricVehicleCharger":
                createElectricVehicleCharger(mapper.convertValue(device, ElectricVehicleChargerDTO.class));
                break;
        }
    }

    private void createElectricVehicleCharger(ElectricVehicleChargerDTO device) {
        
    }

    private void createHouseBattery(HouseBatteryDTO device) {

    }

    private void createSolarPanelSystem(SolarPanelSystemDTO device) {

    }

    private void createVehicleGate(VehicleGateDTO device) {

    }

    private void createSprinklerSystem(SprinklerSystemDTO device) {

    }

    private void createLamp(LampDTO device) {

    }

    private void createWashingMachine(WashingMachineDTO device) {

    }

    private void createAirConditioning(AirConditioningDTO device) {
        AirConditioning newDevice = device.toModel();
//        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        airConditioningRepository.save(newDevice);
    }

    private void createAmbientSensor(AmbientSensorDTO device) {
        AmbientSensor newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        ambientSensorRepository.save(newDevice);
    }
}
