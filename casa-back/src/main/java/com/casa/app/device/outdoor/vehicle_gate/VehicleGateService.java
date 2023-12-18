package com.casa.app.device.outdoor.vehicle_gate;

import com.casa.app.device.measurement.MeasurementType;
import com.casa.app.device.outdoor.lamp.Lamp;
import com.casa.app.device.outdoor.lamp.LampBrightnessMeasurement;
import com.casa.app.device.outdoor.lamp.LampCommandMeasurement;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.device.outdoor.vehicle_gate.dto.VehicleGateSimulationDTO;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.mqtt.MqttGateway;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserService;
import com.casa.app.websocket.SocketMessage;
import com.casa.app.websocket.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class VehicleGateService {

    @Autowired
    private VehicleGateRepository vehicleGateRepository;

    @Autowired
    InfluxDBService influxDBService;
    @Autowired
    private MqttGateway mqttGateway;
    @Autowired
    private RegularUserService regularUserService;
    @Autowired
    private WebSocketController webSocketController;

    public void licencePlatesHandler(Long id, String message) {
        String licencePlate = message;
        VehicleGateLicencePlatesMeasurement vehicleGate = new VehicleGateLicencePlatesMeasurement( id, licencePlate, Instant.now());
        webSocketController.sendMessage(new SocketMessage<>(MeasurementType.vehicleGateLicencePlates, message, id.toString(), id.toString(), vehicleGate));
        influxDBService.write(vehicleGate);
    }

    public void commandHandler(Long id, String message) {
        String user = message.split("\\|")[1];
        Boolean isOpen = Boolean.parseBoolean(message.split("\\|")[0]);
        VehicleGateCommandMeasurement vehicleGate = new VehicleGateCommandMeasurement( id, isOpen, user, Instant.now());
        webSocketController.sendMessage(new SocketMessage<>(MeasurementType.vehicleGateCommand, message, id.toString(), id.toString(), vehicleGate));
        influxDBService.write(vehicleGate);
    }

    public void modeHandler(Long id, String message) {
        String user = message.split("\\|")[1];
        Boolean isPrivate = Boolean.parseBoolean(message.split("\\|")[0]);
        VehicleGateModeMeasurement vehicleGate = new VehicleGateModeMeasurement( id, isPrivate, user, Instant.now());
        webSocketController.sendMessage(new SocketMessage<>(MeasurementType.vehicleGateMode, message, id.toString(), id.toString(), vehicleGate));
        influxDBService.write(vehicleGate);
    }

    public void toggle(Long id, String what) throws NotFoundException, UserNotFoundException {
        RegularUser currentUser = regularUserService.getUserByToken();
        Optional<VehicleGate> gate = vehicleGateRepository.findById(id);
        if (gate.isEmpty()) {
            throw new NotFoundException();
        }
        mqttGateway.sendToMqtt(id + "~" + what.toUpperCase() +"~" + currentUser.getUsername(), id.toString());
    }

    public List<VehicleGateSimulationDTO> getAllSimulation() {
        List<VehicleGate> vehicleGates = vehicleGateRepository.findAll();
        List<VehicleGateSimulationDTO> vehicleGateDTOS = new ArrayList<>();
        for (VehicleGate g : vehicleGates) {
            vehicleGateDTOS.add(new VehicleGateSimulationDTO(g.getId(), g.getAllowedVehicles(), g.getCurrentMode()));
        }
        return vehicleGateDTOS;
    }
}
