package com.casa.app.device.large_electric.electric_vehicle_charger;

import com.casa.app.device.DeviceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricVehicleChargerDTO extends DeviceDTO {
    private int chargePower;
    private int numOfSlots;
}
