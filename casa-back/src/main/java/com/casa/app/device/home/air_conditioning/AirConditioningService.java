package com.casa.app.device.home.air_conditioning;

import com.casa.app.device.Device;
import com.casa.app.device.DeviceRepository;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionModeDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionScheduleDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionTemperatureDTO;
import com.casa.app.device.home.air_conditioning.dtos.AirConditionWorkingDTO;
import com.casa.app.device.home.air_conditioning.measurements.commands.*;
import com.casa.app.device.home.air_conditioning.measurements.execution.AirConditionModeExecution;
import com.casa.app.device.home.air_conditioning.measurements.execution.AirConditionTemperatureExecution;
import com.casa.app.device.home.air_conditioning.measurements.execution.AirConditionWorkingExecution;
import com.casa.app.notifications.Notification;
import com.casa.app.notifications.NotificationRepository;
import com.casa.app.device.home.air_conditioning.schedule.AirConditionSchedule;
import com.casa.app.device.home.air_conditioning.schedule.AirConditionScheduleRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    private AirConditionScheduleRepository airConditionScheduleRepository;
    @Autowired
    private NotificationService notificationService;

    public List<AirConditioningSimulationDTO> getAllSimulation() {
        List<AirConditioning> airConditioners = airConditioningRepository.findAll();
        List<AirConditioningSimulationDTO> airConditionerDTOS = new ArrayList<>();
        for (AirConditioning a : airConditioners) {
            airConditionerDTOS.add(new AirConditioningSimulationDTO(a.getId(), a.getMinTemperature(),
                    a.getMaxTemperature(), a.getSupportedModes()));
        }
        return airConditionerDTOS;
    }

    public void sendCommand(Long deviceId, AirConditionCommand command) throws UserNotFoundException, DeviceNotFoundException {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null) {
            throw new DeviceNotFoundException();
        }
        influxDBService.write(command);
        mqttGateway.sendToMqtt(device.getId()+"~" + command.toMessage(), device.getId().toString());
    }

    public void sendWorkingCommand(AirConditionWorkingDTO dto, RegularUser currentUser) throws UserNotFoundException, DeviceNotFoundException {
        overrideIdNeeded(dto.getId());
        AirConditioningWorkingCommand command = new AirConditioningWorkingCommand(dto.getId(), CommandType.WORKING, dto.isWorking() ? "TURN ON" : "TURN OFF", currentUser.getUsername(), Instant.now());
        sendCommand(dto.getId(), command);
    }

    public void sendTemperatureCommand(AirConditionTemperatureDTO dto, RegularUser currentUser) throws UserNotFoundException, DeviceNotFoundException {
        overrideIdNeeded(dto.getId());
        AirConditioningTemperatureCommand command = new AirConditioningTemperatureCommand(dto.getId(), CommandType.TEMPERATURE, dto.getTemperature(), currentUser.getUsername(), Instant.now());
        sendCommand(dto.getId(), command);
    }

    public void sendModeCommand(AirConditionModeDTO dto, RegularUser currentUser) throws UserNotFoundException, DeviceNotFoundException {
        overrideIdNeeded(dto.getId());
        AirConditioningModeCommand command = new AirConditioningModeCommand(dto.getId(), CommandType.MODE, dto.getMode(), currentUser.getUsername(), Instant.now());
        sendCommand(dto.getId(), command);
    }

    private void overrideIdNeeded(Long deviceId){
        List<AirConditionSchedule> result = airConditionScheduleRepository.getCurrentSchedule(deviceId);
        if(!result.isEmpty()){
            AirConditionSchedule currentSchedule = result.get(0);
            currentSchedule.setOverride(true);
            airConditionScheduleRepository.save(currentSchedule);
        }
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
            AirConditionWorkingExecution result = new AirConditionWorkingExecution(device.getId(), tokens[0], executed, username, Instant.now());
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
            influxDBService.write(result);
            deviceRepository.save(device);
        } catch (NumberFormatException e) {
            return;
        }

    }

    @Scheduled(fixedDelay = 5000)
    public void checkSchedule() {
        List<AirConditionSchedule> schedules = airConditionScheduleRepository.getSchedulesToActivated();
        schedules.stream().parallel().forEach(schedule ->{
            try {
                sendWorkingCommand(new AirConditionWorkingDTO(schedule.isWorking(), schedule.getAirConditioning().getId()), schedule.getAirConditioning().getOwner());
                if(schedule.isWorking()) {
                    if(schedule.getMode() != null) {
                        sendModeCommand(new AirConditionModeDTO(schedule.getMode(), schedule.getAirConditioning().getId()), schedule.getAirConditioning().getOwner());
                    }

                    if(schedule.getTemperature() != null){
                        sendTemperatureCommand(new AirConditionTemperatureDTO(schedule.getTemperature(), schedule.getAirConditioning().getId()), schedule.getAirConditioning().getOwner());
                    }
                }

                schedule.setActivated(true);
                airConditionScheduleRepository.save(schedule);
            } catch (UserNotFoundException e) {
//                TODO
                throw new RuntimeException(e);
            } catch (DeviceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
//        System.out.println(schedules.size());
    }

    public void setSchedule(AirConditionScheduleDTO dto) throws DeviceNotFoundException, InvalidDateException, ScheduleOverlappingException {
        try{
            LocalDateTime start = LocalDateTime.parse(dto.getStartTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime end = LocalDateTime.parse(dto.getEndTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            AirConditioning airConditioning = airConditioningRepository.findById(dto.getDeviceId()).orElse(null);
            if(airConditioning == null){
                throw new DeviceNotFoundException();
            }
            LocalDateTime now = LocalDateTime.now();
            if( end.isBefore(start) || end.isBefore(now) || start.isBefore(now))
                throw new InvalidDateException();
            if(airConditionScheduleRepository.existsOverlaping(start, end, dto.getDeviceId())){
                throw new ScheduleOverlappingException();
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
            airConditionScheduleRepository.save(schedule);

        }catch (NullPointerException e){
            throw new InvalidDateException();
        }

    }
}
