package com.casa.app.device.large_electric.electric_vehicle_charger;

import com.casa.app.device.dto.DeviceDTO;
import com.casa.app.device.DeviceStatus;
import com.casa.app.device.PowerSupplyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricVehicleChargerDTO extends DeviceDTO {
    private int chargePower;
    private int numOfSlots;

    public ElectricVehicleCharger toModel() {
        ElectricVehicleCharger device = new ElectricVehicleCharger();
        device.setName(this.getName());
        device.setStatus(DeviceStatus.OFFLINE);
        device.setEnergyConsumption(this.getEnergyConsumption());
        device.setPowerSupplyType(PowerSupplyType.valueOf(this.getPowerSupplyType()));
        device.setChargePower(this.chargePower);
        device.setNumOfSlots(this.numOfSlots);
        return device;
    }
}
