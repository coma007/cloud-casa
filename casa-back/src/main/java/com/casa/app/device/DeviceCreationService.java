package com.casa.app.device;

import com.casa.app.device.home.air_conditioning.AirConditioning;
import com.casa.app.device.home.air_conditioning.AirConditioningDTO;
import com.casa.app.device.home.air_conditioning.AirConditioningRepository;
import com.casa.app.device.home.ambient_sensor.AmbientSensor;
import com.casa.app.device.home.ambient_sensor.AmbientSensorDTO;
import com.casa.app.device.home.ambient_sensor.AmbientSensorRepository;
import com.casa.app.device.home.washing_machine.WashingMachine;
import com.casa.app.device.home.washing_machine.WashingMachineDTO;
import com.casa.app.device.home.washing_machine.WashingMachineRepository;
import com.casa.app.device.large_electric.electric_vehicle_charger.ElectricVehicleCharger;
import com.casa.app.device.large_electric.electric_vehicle_charger.ElectricVehicleChargerDTO;
import com.casa.app.device.large_electric.electric_vehicle_charger.ElectricVehicleChargerRepository;
import com.casa.app.device.large_electric.house_battery.HouseBattery;
import com.casa.app.device.large_electric.house_battery.HouseBatteryDTO;
import com.casa.app.device.large_electric.house_battery.HouseBatteryRepository;
import com.casa.app.device.large_electric.solar_panel_system.SolarPanelSystem;
import com.casa.app.device.large_electric.solar_panel_system.SolarPanelSystemDTO;
import com.casa.app.device.large_electric.solar_panel_system.SolarPanelSystemRepository;
import com.casa.app.device.outdoor.lamp.Lamp;
import com.casa.app.device.outdoor.lamp.LampDTO;
import com.casa.app.device.outdoor.lamp.LampRepository;
import com.casa.app.device.outdoor.sprinkler_system.SprinklerSystem;
import com.casa.app.device.outdoor.sprinkler_system.SprinklerSystemDTO;
import com.casa.app.device.outdoor.sprinkler_system.SprinklerSystemRepository;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGate;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGateDTO;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGateRepository;
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
    @Autowired
    private WashingMachineRepository washingMachineRepository;
    @Autowired
    private LampRepository lampRepository;
    @Autowired
    private SprinklerSystemRepository sprinklerSystemRepository;
    @Autowired
    private VehicleGateRepository vehicleGateRepository;
    @Autowired
    private ElectricVehicleChargerRepository electricVehicleChargerRepository;
    @Autowired
    private HouseBatteryRepository houseBatteryRepository;
    @Autowired
    private SolarPanelSystemRepository solarPanelSystemRepository;

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
        ElectricVehicleCharger newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        electricVehicleChargerRepository.save(newDevice);
    }

    private void createHouseBattery(HouseBatteryDTO device) {
        HouseBattery newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        houseBatteryRepository.save(newDevice);
    }

    private void createSolarPanelSystem(SolarPanelSystemDTO device) {
        SolarPanelSystem newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        solarPanelSystemRepository.save(newDevice);
    }

    private void createVehicleGate(VehicleGateDTO device) {
        VehicleGate newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        vehicleGateRepository.save(newDevice);
    }

    private void createSprinklerSystem(SprinklerSystemDTO device) {
        SprinklerSystem newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        sprinklerSystemRepository.save(newDevice);
    }

    private void createLamp(LampDTO device) {
        Lamp newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        lampRepository.save(newDevice);
    }

    private void createWashingMachine(WashingMachineDTO device) {
        WashingMachine newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        washingMachineRepository.save(newDevice);
    }

    private void createAirConditioning(AirConditioningDTO device) {
        AirConditioning newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
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
