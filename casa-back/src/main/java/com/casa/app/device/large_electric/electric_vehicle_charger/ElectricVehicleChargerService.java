package com.casa.app.device.large_electric.electric_vehicle_charger;

import com.casa.app.device.large_electric.electric_vehicle_charger.dto.ElectricVehicleChargerSimulationDTO;
import com.casa.app.device.large_electric.electric_vehicle_charger.measurement.ElectricVehicleChargerCommandMeasurement;
import com.casa.app.device.large_electric.electric_vehicle_charger.measurement.ElectricVehicleChargerPowerUsageMeasurement;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.mqtt.MqttGateway;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ElectricVehicleChargerService {

    @Autowired
    private ElectricVehicleChargerRepository electricVehicleChargerRepository;
    @Autowired
    private RegularUserService regularUserService;
    @Autowired
    private MqttGateway mqttGateway;
    @Autowired
    private InfluxDBService influxDBService;


    public void command(Long id, String command, String slot, String maxPercentage) throws UserNotFoundException, NotFoundException {
        RegularUser user = regularUserService.getUserByToken();
        Optional<ElectricVehicleCharger> charger = electricVehicleChargerRepository.findById(id);
        if (charger.isEmpty()) {
            throw new NotFoundException();
        }
        ElectricVehicleChargerCommandMeasurement chargerCommand = new ElectricVehicleChargerCommandMeasurement
                (id, user.getUsername(), generateCommand(command, slot, maxPercentage), Instant.now());
        influxDBService.write(chargerCommand);
        mqttGateway.sendToMqtt(id + "~" + command.toUpperCase().replace(" ", "_") + "~" + slot + "~" + user.getUsername() + "~" + maxPercentage, id.toString());
    }

    private String generateCommand(String command, String slot, String maxPercentage) {
        StringBuilder generatedCommand = new StringBuilder("Command: " + command + ". Slot: " + slot + ". ");
        if (!maxPercentage.equals("/")) {
            generatedCommand.append("Max percentage: ").append(maxPercentage).append("%");
        }
        return generatedCommand.toString();
    }

    //    ID~COMMAND|USER
    public void commandHandler(Long id, String message) {
        String[] tokens = message.split("\\|");
        ElectricVehicleChargerCommandMeasurement chargerCommand = new ElectricVehicleChargerCommandMeasurement
                (id, tokens[1], tokens[0], Instant.now());
        influxDBService.write(chargerCommand);
    }
//    ID~SLOT|POWER
    public void powerUsageHandler(Long id, String message) {
        String[] tokens = message.split("\\|");
        ElectricVehicleChargerPowerUsageMeasurement chargerPowerUsage = new ElectricVehicleChargerPowerUsageMeasurement
                (id, Double.parseDouble(tokens[1]), Integer.parseInt(tokens[0]), Instant.now());
        influxDBService.write(chargerPowerUsage);
    }
    public List<ElectricVehicleChargerSimulationDTO> getAllSimulation() {
        List<ElectricVehicleCharger> chargers = electricVehicleChargerRepository.findAll();
        List<ElectricVehicleChargerSimulationDTO> electricVehicleChargerDTOS = new ArrayList<>();
        for (ElectricVehicleCharger c : chargers) {
            electricVehicleChargerDTOS.add(new ElectricVehicleChargerSimulationDTO(c.getId(), c.getChargePower(), c.getNumOfSlots()));
        }
        return electricVehicleChargerDTOS;
    }
}