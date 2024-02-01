package com.casa.app.device.home.air_conditioning;

import com.casa.app.device.Device;
import com.casa.app.device.DeviceController;
import com.casa.app.device.DeviceRepository;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionModeDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionScheduleDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionTemperatureDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionWorkingDTO;
import com.casa.app.device.home.air_conditioning.measurements.commands.*;
import com.casa.app.device.home.air_conditioning.measurements.execution.AirConditionModeExecution;
import com.casa.app.device.home.air_conditioning.measurements.execution.AirConditionNewScheduleExecution;
import com.casa.app.device.home.air_conditioning.measurements.execution.AirConditionTemperatureExecution;
import com.casa.app.device.home.air_conditioning.measurements.execution.AirConditionWorkingExecution;
import com.casa.app.device.home.air_conditioning.schedule.AirConditioningScheduleRepository;
import com.casa.app.device.home.ambient_sensor.AmbientSensorMeasurement;
import com.casa.app.notifications.Notification;
import com.casa.app.notifications.NotificationRepository;
import com.casa.app.device.home.air_conditioning.schedule.AirConditionSchedule;
import com.casa.app.exceptions.DeviceNotFoundException;
import com.casa.app.exceptions.InvalidDateException;
import com.casa.app.exceptions.ScheduleOverlappingException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.mqtt.MqttGateway;
import com.casa.app.notifications.NotificationService;
import com.casa.app.user.User;
import com.casa.app.user.UserRepository;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserRepository;
import com.casa.app.user.regular_user.RegularUserService;
import com.casa.app.device.home.air_conditioning.dto.AirConditioningSimulationDTO;
import com.casa.app.util.email.JSONUtil;
import com.casa.app.websocket.SocketMessage;
import com.casa.app.websocket.WebSocketController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class AirConditioningService {
    private final static String SUCCESS = "SUCCESS";

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
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private WebSocketController webSocketController;
    @Autowired
    private AirConditioningScheduleRepository airConditioningScheduleRepository;

    public List<AirConditioningSimulationDTO> getAllSimulation() {
        List<AirConditioning> airConditioners = airConditioningRepository.findAll();
        List<AirConditioningSimulationDTO> airConditionerDTOS = new ArrayList<>();
        for (AirConditioning a : airConditioners) {
            airConditionerDTOS.add(new AirConditioningSimulationDTO(a.getId(), a.getMinTemperature(),
                    a.getMaxTemperature(), a.getSupportedModes()));
        }
        return airConditionerDTOS;
    }

    public void sendCommand(Long deviceId, AirConditionCommand command) throws DeviceNotFoundException {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null) {
            throw new DeviceNotFoundException();
        }
        influxDBService.write(command);
        mqttGateway.sendToMqtt(device.getId()+"~" + command.toMessage(), device.getId().toString());
    }

    public void sendWorkingCommand(AirConditionWorkingDTO dto, RegularUser currentUser) throws DeviceNotFoundException {
        AirConditioningWorkingCommand command = new AirConditioningWorkingCommand(dto.getId(), CommandType.WORKING, dto.isWorking() ? "TURN ON" : "TURN OFF", currentUser.getUsername(), Instant.now());
        sendCommand(dto.getId(), command);
    }

    public void sendTemperatureCommand(AirConditionTemperatureDTO dto, RegularUser currentUser) throws DeviceNotFoundException {
        AirConditioningTemperatureCommand command = new AirConditioningTemperatureCommand(dto.getId(), CommandType.TEMPERATURE, dto.getTemperature(), currentUser.getUsername(), Instant.now());
        sendCommand(dto.getId(), command);
    }

    public void sendModeCommand(AirConditionModeDTO dto, RegularUser currentUser) throws DeviceNotFoundException {
        AirConditioningModeCommand command = new AirConditioningModeCommand(dto.getId(), CommandType.MODE, dto.getMode(), currentUser.getUsername(), Instant.now());
        sendCommand(dto.getId(), command);
    }


    public void handleWorkingAckMessage(Long id, String message){
//        System.out.println("===============================" + message);

        String[] tokens = message.split("\\|");
        boolean working = tokens[1].equalsIgnoreCase("TURN ON");
        String username = tokens[2];
        String executed = tokens[3];
        boolean exec = executed.equalsIgnoreCase(SUCCESS);

        AirConditioning device = airConditioningRepository.findById(id).orElse(null);
        User user = userRepository.findByUsername(username).orElse(null);
        if (device == null || user == null) {
            return;
        }
        try {
//            device.setWorking(working);
            notificationService.makeNotification(user,
                    "Setting air condition " + (working ? "ON" : "OFF") + ", was " + (exec ? "successful" : "failure") );
            AirConditionWorkingExecution result = new AirConditionWorkingExecution(device.getId(), tokens[1], executed, username, Instant.now());
            webSocketController.sendMessage(new SocketMessage<AirConditionWorkingExecution>("air_conditioning_commands", "New value", null, id.toString(), result));
            influxDBService.write(result);
            deviceRepository.save(device);
        } catch (NumberFormatException e) {
            return;
        }

    }

    public void handleTemperatureAckMessage(Long id, String message){
//        System.out.println("===============================" + message);

        String[] tokens = message.split("\\|");
        double temperature = Double.parseDouble(tokens[1].trim());
        String username = tokens[2];
        String executed = tokens[3];
        boolean exec = executed.equalsIgnoreCase(SUCCESS);

        AirConditioning device = airConditioningRepository.findById(id).orElse(null);
        User user = userRepository.findByUsername(username).orElse(null);
        if (device == null || user == null) {
            return;
        }
        try {
//            device.setCurrentTargetTemperature(temperature);
            notificationService.makeNotification(user, "Setting air condition temperature to "
                    + temperature + ", was " + (exec ? "successful" : "failure") );
            AirConditionTemperatureExecution result = new AirConditionTemperatureExecution(device.getId(), temperature, executed, username, Instant.now());
            webSocketController.sendMessage(new SocketMessage<AirConditionTemperatureExecution>("air_conditioning_commands", "New value", null, id.toString(), result));
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

        AirConditioning device = airConditioningRepository.findById(id).orElse(null);
        User user = userRepository.findByUsername(username).orElse(null);
        if (device == null || user == null) {
            return;
        }
        try {
//            device.setMode(mode);
            notificationService.makeNotification(user, "Setting air condition mode to "
                    + mode + ", was " + (exec ? "successful" : "failure"));
            AirConditionModeExecution result = new AirConditionModeExecution(device.getId(), mode, executed, username, Instant.now());
            webSocketController.sendMessage(new SocketMessage<AirConditionModeExecution>("air_conditioning_commands", "New value", null, id.toString(), result));
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

        AirConditioning device = airConditioningRepository.findById(id).orElse(null);
        User user = userRepository.findByUsername(username).orElse(null);
        if (device == null || user == null) {
            return;
        }
        try {
            if(exec){
                AirConditionSchedule sch = JSONUtil.readAirCon(schedule);
                sch.setAirConditioning(device);
                airConditioningScheduleRepository.save(sch);
            }

            notificationService.makeNotification(user, "Setting schedule was " + (exec ? "successful" : "failure"));
            AirConditionNewScheduleExecution result = new AirConditionNewScheduleExecution(device.getId(), schedule, executed, username, Instant.now());
            webSocketController.sendMessage(new SocketMessage<AirConditionNewScheduleExecution>("air_conditioning_commands", "New value", null, id.toString(), result));
            influxDBService.write(result);
            deviceRepository.save(device);
        } catch (NumberFormatException e) {
            return;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public void setSchedule(AirConditionScheduleDTO dto, User currentUser) throws DeviceNotFoundException, InvalidDateException, ScheduleOverlappingException, UserNotFoundException {
        try{
            LocalDateTime start = LocalDateTime.parse(dto.getStartTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime end = LocalDateTime.parse(dto.getEndTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            AirConditioning airConditioning = airConditioningRepository.findById(dto.getDeviceId()).orElse(null);
            if(airConditioning == null){
                throw new DeviceNotFoundException();
            }

            AirConditionSchedule schedule = new AirConditionSchedule();
            schedule.setStartTime(start);
            schedule.setEndTime(end);
            schedule.setOverride(false);
            schedule.setAirConditioning(airConditioning);
            schedule.setTemperature(dto.getTemperature());
            schedule.setMode(dto.getMode());
            schedule.setActivated(false);
            schedule.setWorking(dto.isWorking());
            schedule.setRepeating(dto.isRepeating());
            schedule.setRepeatingDaysIncrement(dto.getRepeatingDaysIncrement());

            String json = JSONUtil.getJsonWithDate(schedule);

            AirConditionNewScheduleCommand command = new AirConditionNewScheduleCommand(dto.getDeviceId(), CommandType.NEW_SCHEDULE, json,  currentUser.getUsername(), Instant.now());
            sendCommand(dto.getDeviceId(), command);


        }catch (NullPointerException e){
            throw new InvalidDateException();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


    public List<AirConditionSchedule> getSchedules(Long deviceId) {
        return airConditioningScheduleRepository.findByAirConditioning_Id(deviceId);
    }
}
