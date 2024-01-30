package com.casa.app.device.large_electric.electric_vehicle_charger;

import com.casa.app.device.large_electric.electric_vehicle_charger.dto.ElectricVehicleChargerSimulationDTO;
import com.casa.app.device.large_electric.electric_vehicle_charger.measurement.ElectricVehicleChargerCommandMeasurement;
import com.casa.app.device.large_electric.electric_vehicle_charger.measurement.ElectricVehicleChargerPowerUsageMeasurement;
import com.casa.app.influxdb.InfluxDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElectricVehicleChargerService {

    @Autowired
    private ElectricVehicleChargerRepository electricVehicleChargerRepository;

    @Autowired
    private InfluxDBService influxDBService;

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
