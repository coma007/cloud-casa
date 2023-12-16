package com.casa.app.device.home.air_conditioning;

import com.casa.app.device.Device;
import com.casa.app.device.DeviceRepository;
import com.casa.app.device.DeviceStatus;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionModeDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionTemperatureDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionWorkingDTO;
import com.casa.app.device.home.air_conditioning.measurements.commands.*;
import com.casa.app.device.home.air_conditioning.measurements.execution.AirConditionModeExecution;
import com.casa.app.device.home.air_conditioning.measurements.execution.AirConditionTemperatureExecution;
import com.casa.app.device.home.air_conditioning.measurements.execution.AirConditionWorkingExecution;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.mqtt.MqttGateway;
import com.casa.app.user.User;
import com.casa.app.user.UserRepository;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserRepository;
import com.casa.app.user.regular_user.RegularUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class AirConditioningService {

    @Autowired
    private AirConditioningRepository airConditioningRepository;

    @Autowired
    private InfluxDBService influxDBService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private RegularUserService regularUserService;

    @Autowired
    private MqttGateway mqttGateway;
    private CountDownLatch processingFinishedLatch;
    @Autowired
    private RegularUserRepository regularUserRepository;
    @Autowired
    private UserRepository userRepository;

    public List<AirConditioningSimulationDTO> getAllSimulation() {
        List<AirConditioning> airConditioners = airConditioningRepository.findAll();
        List<AirConditioningSimulationDTO> airConditionerDTOS = new ArrayList<>();
        for (AirConditioning a : airConditioners) {
            airConditionerDTOS.add(new AirConditioningSimulationDTO(a.getId(), a.getMinTemperature(),
                    a.getMaxTemperature(), a.getSupportedModes()));
        }
        return airConditionerDTOS;
    }

    public void sendCommand(Long deviceId, AirConditionCommand command) throws UserNotFoundException {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null) {
            return;
        }
        influxDBService.write(command);
        mqttGateway.sendToMqtt(device.getId()+"~" + command.toMessage(), device.getId().toString());
    }

    public void sendWorkingCommand(AirConditionWorkingDTO dto) throws UserNotFoundException {
        RegularUser currentUser = regularUserService.getUserByToken();
        AirConditioningWorkingCommand command = new AirConditioningWorkingCommand(dto.getId(), CommandType.WORKING, dto.isWorking() ? "TURN ON" : "TURN OFF", currentUser.getUsername(), Instant.now());
        sendCommand(dto.getId(), command);
    }

    public void sendTemperatureCommand(AirConditionTemperatureDTO dto) throws UserNotFoundException {
        RegularUser currentUser = regularUserService.getUserByToken();
        AirConditioningTemperatureCommand command = new AirConditioningTemperatureCommand(dto.getId(), CommandType.TEMPERATURE, dto.getTemperature(), currentUser.getUsername(), Instant.now());
        sendCommand(dto.getId(), command);
    }

    public void sendModeCommand(AirConditionModeDTO dto) throws UserNotFoundException {
        RegularUser currentUser = regularUserService.getUserByToken();
        AirConditioningModeCommand command = new AirConditioningModeCommand(dto.getId(), CommandType.MODE, dto.getMode(), currentUser.getUsername(), Instant.now());
        sendCommand(dto.getId(), command);
    }

    public void handleWorkingAckMessage(Long id, String message){
        System.out.println("===============================" + message);

        String[] tokens = message.split("\\|");
        boolean working = tokens[1].equalsIgnoreCase("TURN ON");
        String username = tokens[2];
        String executed = tokens[3];

        Device device = deviceRepository.findById(id).orElse(null);
        User user = userRepository.findByUsername(username).orElse(null);
        if (device == null || user == null) {
            return;
        }
        try {
            device.setStatus(working ? DeviceStatus.ONLINE : DeviceStatus.OFFLINE);
            AirConditionWorkingExecution result = new AirConditionWorkingExecution(device.getId(), tokens[0], executed, username, Instant.now());
            influxDBService.write(result);
            deviceRepository.save(device);
        } catch (NumberFormatException e) {
            return;
        }

    }

    public void handleTemperatureAckMessage(Long id, String message){
        System.out.println("===============================" + message);

        String[] tokens = message.split("\\|");
        double temperature = Double.parseDouble(tokens[1].trim());
        String username = tokens[2];
        String executed = tokens[3];

        Device device = deviceRepository.findById(id).orElse(null);
        User user = userRepository.findByUsername(username).orElse(null);
        if (device == null || user == null) {
            return;
        }
        try {
//            device.setStatus(working ? DeviceStatus.ONLINE : DeviceStatus.OFFLINE);
            AirConditionTemperatureExecution result = new AirConditionTemperatureExecution(device.getId(), temperature, executed, username, Instant.now());
            influxDBService.write(result);
            deviceRepository.save(device);
        } catch (NumberFormatException e) {
            return;
        }

    }

    public void handleModeAckMessage(Long id, String message){
        System.out.println("===============================" + message);

        String[] tokens = message.split("\\|");
        String mode = tokens[1];
        String username = tokens[2];
        String executed = tokens[3];

        AirConditioning device = airConditioningRepository.findById(id).orElse(null);
        User user = userRepository.findByUsername(username).orElse(null);
        if (device == null || user == null) {
            return;
        }
        try {
//            device.set.setStatus(working ? DeviceStatus.ONLINE : DeviceStatus.OFFLINE);
            AirConditionModeExecution result = new AirConditionModeExecution(device.getId(), mode, executed, username, Instant.now());
            influxDBService.write(result);
            deviceRepository.save(device);
        } catch (NumberFormatException e) {
            return;
        }

    }
}
