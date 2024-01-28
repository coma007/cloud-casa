package com.casa.app.device.large_electric.electric_vehicle_charger;

import com.casa.app.device.large_electric.electric_vehicle_charger.dto.ElectricVehicleChargerSimulationDTO;
import com.casa.app.device.large_electric.house_battery.HouseBattery;
import com.casa.app.device.large_electric.house_battery.dto.HouseBatterySimulationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ElectricVehicleChargerService {

    @Autowired
    private ElectricVehicleChargerRepository electricVehicleChargerRepository;

    public List<ElectricVehicleChargerSimulationDTO> getAllSimulation() {
        List<ElectricVehicleCharger> chargers = electricVehicleChargerRepository.findAll();
        List<ElectricVehicleChargerSimulationDTO> electricVehicleChargerDTOS = new ArrayList<>();
        for (ElectricVehicleCharger c : chargers) {
            electricVehicleChargerDTOS.add(new ElectricVehicleChargerSimulationDTO(c.getId(), c.getChargePower(), c.getNumOfSlots()));
        }
        return electricVehicleChargerDTOS;
    }
}
