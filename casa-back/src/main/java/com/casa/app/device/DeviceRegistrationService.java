package com.casa.app.device;

import com.casa.app.device.dto.DeviceRegistrationDTO;
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
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceRegistrationService {

    @Autowired
    private RealEstateService realEstateService;
    @Autowired
    private RegularUserService regularUserService;

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

    @Transactional
    public void registerDevice(DeviceRegistrationDTO deviceDTO) {
        String type = deviceDTO.getType();
        Object device = deviceDTO.getDevice();
        ObjectMapper mapper = new ObjectMapper();
        // Determine the type of device and handle creation accordingly
        switch (type) {
            case "AmbientSensor":
                registerAmbientSensor(mapper.convertValue(device, AmbientSensorDTO.class));
                break;
            case "AirConditioning":
                registerAirConditioning(mapper.convertValue(device, AirConditioningDTO.class));
                break;
            case "WashingMachine":
                registerWashingMachine(mapper.convertValue(device, WashingMachineDTO.class));
                break;
            case "Lamp":
                registerLamp(mapper.convertValue(device, LampDTO.class));
                break;
            case "SprinklerSystem":
                registerSprinklerSystem(mapper.convertValue(device, SprinklerSystemDTO.class));
                break;
            case "VehicleGate":
                registerVehicleGate(mapper.convertValue(device, VehicleGateDTO.class));
                break;
            case "SolarPanelSystem":
                registerSolarPanelSystem(mapper.convertValue(device, SolarPanelSystemDTO.class));
                break;
            case "HouseBattery":
                registerHouseBattery(mapper.convertValue(device, HouseBatteryDTO.class));
                break;
            case "ElectricVehicleCharger":
                registerElectricVehicleCharger(mapper.convertValue(device, ElectricVehicleChargerDTO.class));
                break;
        }
    }

    private void registerElectricVehicleCharger(ElectricVehicleChargerDTO device) {
        ElectricVehicleCharger newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        RegularUser currentUser = regularUserService.getUserByToken();
        newDevice.setOwner(currentUser);
        electricVehicleChargerRepository.save(newDevice);
    }

    private void registerHouseBattery(HouseBatteryDTO device) {
        HouseBattery newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        RegularUser currentUser = regularUserService.getUserByToken();
        newDevice.setOwner(currentUser);
        houseBatteryRepository.save(newDevice);
    }

    private void registerSolarPanelSystem(SolarPanelSystemDTO device) {
        SolarPanelSystem newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        RegularUser currentUser = regularUserService.getUserByToken();
        newDevice.setOwner(currentUser);
        solarPanelSystemRepository.save(newDevice);
    }

    private void registerVehicleGate(VehicleGateDTO device) {
        VehicleGate newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        RegularUser currentUser = regularUserService.getUserByToken();
        newDevice.setOwner(currentUser);
        vehicleGateRepository.save(newDevice);
    }

    private void registerSprinklerSystem(SprinklerSystemDTO device) {
        SprinklerSystem newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        RegularUser currentUser = regularUserService.getUserByToken();
        newDevice.setOwner(currentUser);
        sprinklerSystemRepository.save(newDevice);
    }

    private void registerLamp(LampDTO device) {
        Lamp newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        RegularUser currentUser = regularUserService.getUserByToken();
        newDevice.setOwner(currentUser);
        lampRepository.save(newDevice);
    }

    private void registerWashingMachine(WashingMachineDTO device) {
        WashingMachine newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        RegularUser currentUser = regularUserService.getUserByToken();
        newDevice.setOwner(currentUser);
        washingMachineRepository.save(newDevice);
    }

    private void registerAirConditioning(AirConditioningDTO device) {
        AirConditioning newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        RegularUser currentUser = regularUserService.getUserByToken();
        newDevice.setOwner(currentUser);
        airConditioningRepository.save(newDevice);
    }

    private void registerAmbientSensor(AmbientSensorDTO device) {
        AmbientSensor newDevice = device.toModel();
        newDevice.setRealEstate(realEstateService.getByName(device.getRealEstateName()));
        newDevice.setCredentials(new ConnectionCredentials());
        RegularUser currentUser = regularUserService.getUserByToken();
        newDevice.setOwner(currentUser);
        ambientSensorRepository.save(newDevice);
    }
}
