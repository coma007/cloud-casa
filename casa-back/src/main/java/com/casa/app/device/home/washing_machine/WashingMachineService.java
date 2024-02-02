package com.casa.app.device.home.washing_machine;

import com.casa.app.device.Device;
import com.casa.app.device.DeviceRepository;
import com.casa.app.device.home.washing_machine.dto.WashingMachineModeDTO;
import com.casa.app.device.home.washing_machine.dto.WashingMachineScheduleDTO;
import com.casa.app.device.home.washing_machine.dto.WashingMachineSimulationDTO;
import com.casa.app.device.home.washing_machine.dto.WashingMachineWorkingDTO;
import com.casa.app.device.home.washing_machine.measuraments.commands.*;
import com.casa.app.device.home.washing_machine.measuraments.execution.WashingMachineExecution;
import com.casa.app.device.home.washing_machine.schedule.WashingMachineSchedule;
import com.casa.app.device.home.washing_machine.schedule.WashingMachineScheduleRepository;
import com.casa.app.exceptions.DeviceNotFoundException;
import com.casa.app.exceptions.InvalidDateException;
import com.casa.app.exceptions.ScheduleOverlappingException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.mqtt.MqttGateway;
import com.casa.app.notifications.NotificationService;
import com.casa.app.user.User;
import com.casa.app.user.UserRepository;
import com.casa.app.user.regular_user.RegularUserRepository;
import com.casa.app.user.regular_user.RegularUserService;
import com.casa.app.util.email.JSONUtil;
import com.casa.app.websocket.SocketMessage;
import com.casa.app.websocket.WebSocketController;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WashingMachineService {
    private final static String SUCCESS = "SUCCESS";


    @Autowired
    private InfluxDBService influxDBService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private RegularUserService regularUserService;

    @Autowired
    private MqttGateway mqttGateway;
    @Autowired
    private RegularUserRepository regularUserRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private WebSocketController webSocketController;
    @Autowired
    WashingMachineRepository washingMachineRepository;

    @Autowired
    WashingMachineScheduleRepository washingMachineScheduleRepository;

    public List<WashingMachineSimulationDTO> getAllSimulation() {
        List<WashingMachine> washingMachines = washingMachineRepository.findAll();
        List<WashingMachineSimulationDTO> washingMachineDTOS = new ArrayList<>();
        for (WashingMachine a : washingMachines) {
            washingMachineDTOS.add(new WashingMachineSimulationDTO(a.getId(), a.getSupportedMods()));
        }
        return washingMachineDTOS;
    }

    public void sendCommand(Long deviceId, WashingMachineCommand command) throws DeviceNotFoundException {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null) {
            throw new DeviceNotFoundException();
        }
        influxDBService.write(command);
        mqttGateway.sendToMqtt(device.getId()+"~" + command.toMessage(), device.getId().toString());
    }

    public void sendWorkingCommand(WashingMachineWorkingDTO dto, User currentUser) throws DeviceNotFoundException {
        WashingMachineWorkingCommand command = new WashingMachineWorkingCommand(dto.getId(), CommandType.WORKING, dto.isWorking() ? "TURN ON" : "TURN OFF", currentUser.getUsername(), Instant.now());
        sendCommand(dto.getId(), command);
    }
    
    public void sendModeCommand(WashingMachineModeDTO dto, User currentUser) throws DeviceNotFoundException {
        WashingMachineModeCommand command = new WashingMachineModeCommand(dto.getId(), CommandType.MODE, dto.getMode(), currentUser.getUsername(), Instant.now());
        sendCommand(dto.getId(), command);
    }

    public void handleWorkingAckMessage(Long id, String message){
//        System.out.println("===============================" + message);

        String[] tokens = message.split("\\|");
        boolean working = tokens[1].equalsIgnoreCase("TURN ON");
        String username = tokens[2];
        String executed = tokens[3];
        boolean exec = executed.equalsIgnoreCase(SUCCESS);

        WashingMachine device = washingMachineRepository.findById(id).orElse(null);
        User user = userRepository.findByUsername(username).orElse(null);
        if (device == null || user == null) {
            return;
        }
        try {
//            device.setWorking(working);
            notificationService.makeNotification(user,
                    "Setting washing machine " + (working ? "ON" : "OFF") + ", was " + (exec ? "successful" : "failure") );
            WashingMachineExecution result = new WashingMachineExecution(device.getId(), tokens[1], executed, username, Instant.now());
            webSocketController.sendMessage(new SocketMessage<WashingMachineExecution>("washing_machine_commands", "New value", null, id.toString(), result));
            influxDBService.write(result);
            deviceRepository.save(device);
        } catch (NumberFormatException e) {
            return;
        }

    }

    public void handleModeAckMessage(Long id, String message){
//        System.out.println("===============================" + message);

        String[] tokens = message.split("\\|");
        String mode = tokens[1];
        String username = tokens[2];
        String executed = tokens[3];
        boolean exec = executed.equalsIgnoreCase(SUCCESS);

        WashingMachine device = washingMachineRepository.findById(id).orElse(null);
        User user = userRepository.findByUsername(username).orElse(null);
        if (device == null || user == null) {
            return;
        }
        try {
//            device.setMode(mode);
            notificationService.makeNotification(user, "Setting washing machine mode to "
                    + mode + ", was " + (exec ? "successful" : "failure"));
            WashingMachineExecution result = new WashingMachineExecution(device.getId(), mode, executed, username, Instant.now());
            webSocketController.sendMessage(new SocketMessage<WashingMachineExecution>("washing_machine_commands", "New value", null, id.toString(), result));
            influxDBService.write(result);
            deviceRepository.save(device);
        } catch (NumberFormatException e) {
            return;
        }

    }

    public void handleNewScheduleAckMessage(Long id, String content) {
        System.out.println("===============================" + content);

        String[] tokens = content.split("\\|");
        String schedule = tokens[1];
        String username = tokens[2];
        String executed = tokens[3];
        boolean exec = executed.equalsIgnoreCase(SUCCESS);

        WashingMachine device = washingMachineRepository.findById(id).orElse(null);
        User user = userRepository.findByUsername(username).orElse(null);
        if (device == null || user == null) {
            return;
        }
        try {
            if(exec){
                WashingMachineSchedule sch = JSONUtil.readWashingMachine(schedule);
                sch.setWashingMachine(device);
                washingMachineScheduleRepository.save(sch);
            }

            notificationService.makeNotification(user, "Setting schedule was " + (exec ? "successful" : "failure"));
            WashingMachineExecution result = new WashingMachineExecution(device.getId(), schedule, executed, username, Instant.now());
            webSocketController.sendMessage(new SocketMessage<WashingMachineExecution>("washing_machine_commands", "New value", null, id.toString(), result));
            influxDBService.write(result);
            deviceRepository.save(device);
        } catch (NumberFormatException e) {
            return;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSchedule(WashingMachineScheduleDTO dto, User currentUser) throws DeviceNotFoundException, InvalidDateException, ScheduleOverlappingException, UserNotFoundException {
        try{
            LocalDateTime start = LocalDateTime.parse(dto.getStartTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime end = LocalDateTime.parse(dto.getEndTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            WashingMachine WashingMachine = washingMachineRepository.findById(dto.getDeviceId()).orElse(null);
            if(WashingMachine == null){
                throw new DeviceNotFoundException();
            }

            WashingMachineSchedule schedule = new WashingMachineSchedule();
            schedule.setStartTime(start);
            schedule.setEndTime(end);
            schedule.setWashingMachine(WashingMachine);
            schedule.setMode(dto.getMode());
            schedule.setActivated(false);
            schedule.setWorking(dto.isWorking());

            String json = JSONUtil.getJsonWithDate(schedule);

            WashingMachineNewScheduleCommand command = new WashingMachineNewScheduleCommand(dto.getDeviceId(), CommandType.NEW_SCHEDULE, json,  currentUser.getUsername(), Instant.now());
            sendCommand(dto.getDeviceId(), command);


        }catch (NullPointerException e){
            throw new InvalidDateException();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public List<WashingMachineSchedule> getSchedules(Long deviceId) {
        return washingMachineScheduleRepository.findByWashingMachine_Id(deviceId);
    }
}
