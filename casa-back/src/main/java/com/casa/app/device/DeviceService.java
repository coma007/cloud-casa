package com.casa.app.device;

import com.casa.app.device.dto.DeviceDetailsDTO;
import com.casa.app.device.dto.DeviceSimulationDTO;
import com.casa.app.device.home.air_conditioning.AirConditioning;
import com.casa.app.device.home.air_conditioning.dto.AirConditioningDetailsDTO;
import com.casa.app.device.home.ambient_sensor.AmbientSensor;
import com.casa.app.device.home.ambient_sensor.dto.AmbientSensorDetailsDTO;
import com.casa.app.device.home.washing_machine.WashingMachine;
import com.casa.app.device.home.washing_machine.dto.WashingMachineDetailsDTO;
import com.casa.app.device.large_electric.electric_vehicle_charger.ElectricVehicleCharger;
import com.casa.app.device.large_electric.electric_vehicle_charger.dto.ElectricVehicleChargerDetailsDTO;
import com.casa.app.device.large_electric.house_battery.HouseBattery;
import com.casa.app.device.large_electric.house_battery.dto.HouseBatteryDetailsDTO;
import com.casa.app.device.large_electric.solar_panel_system.SolarPanelSystem;
import com.casa.app.device.large_electric.solar_panel_system.dto.SolarPanelSystemDetailsDTO;
import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementList;
import com.casa.app.device.measurement.OnlineMeasurementList;
import com.casa.app.device.outdoor.lamp.Lamp;
import com.casa.app.device.outdoor.lamp.dto.LampDetailsDTO;
import com.casa.app.device.outdoor.sprinkler_system.SprinklerSystem;
import com.casa.app.device.outdoor.sprinkler_system.dto.SprinklerSystemDetailsDTO;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGate;
import com.casa.app.device.outdoor.vehicle_gate.dto.VehicleGateDetailsDTO;
import com.casa.app.estate.RealEstate;
import com.casa.app.estate.RealEstateService;
import com.casa.app.exceptions.UnathorizedException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.permission.PermissionService;
import com.casa.app.user.User;
import com.casa.app.user.UserService;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.min;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private RealEstateService realEstateService;

    @Autowired
    private InfluxDBService influxDBService;

    @Autowired
    private UserService userService;
    @Autowired
    private RegularUserService regularUserService;
    @Autowired
    private PermissionService permissionService;

    public List<DeviceDetailsDTO> getAllByOwner() throws UserNotFoundException {
        List<Device> devices;
        try {
            RegularUser currentUser = regularUserService.getUserByToken();
            devices = deviceRepository.findAllByOwner(currentUser);
            devices = devices
                    .parallelStream()
                    .filter(d->permissionService.canReadDevice(d.getId(), currentUser.getId()))
                    .collect(Collectors.toList());
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
        List<DeviceDetailsDTO> devicesDTO = new ArrayList<>();
        for (Device d : devices) {
            DeviceDetailsDTO dto = d.toDetailsDTO();
            dto.setType(getType(d));
            devicesDTO.add(dto);
        }
        return devicesDTO;
    }

    public List<DeviceDetailsDTO> getAllByRealEstate(Long realEstateId) throws UserNotFoundException {
        RealEstate realEstate = realEstateService.getById(realEstateId);
        if (realEstate == null) {
            return null;
        }
        List<Device> devices = deviceRepository.findAllByRealEstate(realEstate);
        RegularUser currentUser = regularUserService.getUserByToken();
        devices = devices
                .parallelStream()
                .filter(d->permissionService.canReadDevice(d.getId(), currentUser.getId()))
                .collect(Collectors.toList());
        List<DeviceDetailsDTO> devicesDTO = new ArrayList<>();
        for (Device d : devices) {
            DeviceDetailsDTO dto = d.toDetailsDTO();
            dto.setType(getType(d));
            devicesDTO.add(dto);
        }
        return devicesDTO;
    }

    public DeviceDetailsDTO getDeviceDetails(Long deviceId) throws UserNotFoundException, UnathorizedException {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        RegularUser currentUser = regularUserService.getUserByToken();
        if(permissionService.canReadDevice(deviceId, currentUser.getId()))
            return getDeviceDetailsDTO(device);
        else
            throw new UnathorizedException();
    }

    private static DeviceDetailsDTO getDeviceDetailsDTO(Device device) {
        if (device instanceof AirConditioning) {
            AirConditioningDetailsDTO dto = ((AirConditioning) device).toDetailsDTO();
            dto.setType(getType(device));
            return dto;
        } else if (device instanceof WashingMachine) {
            WashingMachineDetailsDTO dto = ((WashingMachine) device).toDetailsDTO();
            dto.setType(getType(device));
            return dto;
        } else if (device instanceof AmbientSensor) {
            AmbientSensorDetailsDTO dto = ((AmbientSensor) device).toDetailsDTO();
            dto.setType(getType(device));
            return dto;
        } else if (device instanceof Lamp) {
            LampDetailsDTO dto = ((Lamp) device).toDetailsDTO();
            dto.setType(getType(device));
            return dto;
        } else if (device instanceof SprinklerSystem) {
            SprinklerSystemDetailsDTO dto = ((SprinklerSystem) device).toDetailsDTO();
            dto.setType(getType(device));
            return dto;
        } else if (device instanceof VehicleGate) {
            VehicleGateDetailsDTO dto = ((VehicleGate) device).toDetailsDTO();
            dto.setType(getType(device));
            return dto;
        } else if (device instanceof SolarPanelSystem) {
            SolarPanelSystemDetailsDTO dto = ((SolarPanelSystem) device).toDetailsDTO();
            dto.setType(getType(device));
            return dto;
        } else if (device instanceof HouseBattery) {
            HouseBatteryDetailsDTO dto = ((HouseBattery) device).toDetailsDTO();
            dto.setType(getType(device));
            return dto;
        } else {
            ElectricVehicleChargerDetailsDTO dto = ((ElectricVehicleCharger) device).toDetailsDTO();
            dto.setType(getType(device));
            return dto;
        }
    }

    public List<DeviceSimulationDTO> getAllSimulation() {
        List<Device> devices = deviceRepository.findAll();
        List<DeviceSimulationDTO> devicesDTO = new ArrayList<>();
        for (Device d : devices) {
            String type = getType(d);
            devicesDTO.add(new DeviceSimulationDTO(d.getId(), type));
        }
        return devicesDTO;
    }

    public List<DeviceSimulationDTO> getAll() throws UserNotFoundException {
        List<Device> devices = deviceRepository.findAll();
        RegularUser currentUser = regularUserService.getUserByToken();
        devices = devices
                .parallelStream()
                .filter(d->permissionService.canReadDevice(d.getId(), currentUser.getId()))
                .collect(Collectors.toList());
        List<DeviceSimulationDTO> devicesDTO = new ArrayList<>();
        for (Device d : devices) {
            String type = getType(d);
            devicesDTO.add(new DeviceSimulationDTO(d.getId(), type));
        }
        return devicesDTO;
    }

    private static String getType(Device d) {
        String type = "solar_panel_system";
        if (d instanceof AmbientSensor) {
            type = "ambient_sensor";
        } else if (d instanceof AirConditioning) {
            type = "air_conditioning";
        } else if (d instanceof WashingMachine) {
            type = "washing_machine";
        } else if (d instanceof Lamp) {
            type = "lamp";
        } else if (d instanceof SprinklerSystem) {
            type = "sprinkler_system";
        } else if (d instanceof VehicleGate) {
            type = "vehicle_gate";
        } else if (d instanceof ElectricVehicleCharger) {
            type = "electric_vehicle_charger";
        } else if (d instanceof HouseBattery) {
            type = "house_battery";
        }
        return type;
    }

    public MeasurementList queryMeasurements(Long id, String measurement, String from, String to, String username, int page) {
        Device device = deviceRepository.getReferenceById(id);
        Instant fromDate = null, toDate = null;
        if (!from.equals("")) {
            fromDate = Instant.parse(from);
        }
        if (!to.equals("")) {
            toDate = Instant.parse(to);
        }
        boolean findUser = true;
        if (username.equals("")) {
            findUser = false;
        }
        User user = userService.getByUsername(username);
        MeasurementList data = influxDBService.query(measurement, device, fromDate, toDate, user, findUser);
        if (data.getMeasurements().size() != 0) {
            Collections.reverse(data.getMeasurements());
            int firstIndex = min(10 * (page - 1), data.getMeasurements().size());
            int lastIndex = min(10 * page, data.getMeasurements().size());
            List<AbstractMeasurement> newData = data.getMeasurements().subList(firstIndex, lastIndex);
            data.setMeasurements(newData);
        }
        return data;
    }

    public int queryNumOfPages(Long id, String measurement, String from, String to, String username) {
        Device device = deviceRepository.getReferenceById(id);
        Instant fromDate = null;
        if (!from.equals("")) {
            fromDate = Instant.parse(from);
        }
        Instant toDate = null;
        if (!to.equals("")) {
            toDate = Instant.parse(to);
        }
        boolean findUser = true;
        if (username.equals("")) {
            findUser = false;
        }
        User user = userService.getByUsername(username);
        MeasurementList data = influxDBService.query(measurement, device, fromDate, toDate, user, findUser);
        return (data.getMeasurements().size() == 0) ? 0 : (data.getMeasurements().size() / 10) + 1;
    }

    public OnlineMeasurementList queryActivity(Long id, String from, String to) {
        Device device = deviceRepository.getReferenceById(id);
        Instant fromDate = null;
        if (!from.equals("")) {
            fromDate = Instant.parse(from);
        }
        Instant toDate = null;
        if (!to.equals("")) {
            toDate = Instant.parse(to);
        }
        return influxDBService.queryActivity(device, fromDate, toDate);
    }
}
