package com.casa.app.device.large_electric.electric_vehicle_charger.dto;

import com.casa.app.device.dto.DeviceDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricVehicleChargerDetailsDTO extends DeviceDetailsDTO {
    private int chargePower;
    private int numOfSlots;

}
