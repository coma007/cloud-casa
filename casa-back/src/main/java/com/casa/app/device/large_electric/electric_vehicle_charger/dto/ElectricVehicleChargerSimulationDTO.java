package com.casa.app.device.large_electric.electric_vehicle_charger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricVehicleChargerSimulationDTO {
    private Long id;
    private int chargePower;
    private int numOfSlots;
}
