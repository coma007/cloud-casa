package com.casa.app.device.large_electric.house_battery;

import com.casa.app.device.DeviceDTO;
import com.casa.app.device.DeviceStatus;
import com.casa.app.device.PowerSupplyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseBatteryDTO extends DeviceDTO {
    private double size;

    public HouseBattery toModel() {
        HouseBattery device = new HouseBattery();
        device.setName(this.getName());
        device.setStatus(DeviceStatus.OFFLINE);
        device.setEnergyConsumption(this.getEnergyConsumption());
        device.setPowerSupplyType(PowerSupplyType.valueOf(this.getPowerSupplyType()));
        device.setSize(this.size);
        return device;
    }
}
