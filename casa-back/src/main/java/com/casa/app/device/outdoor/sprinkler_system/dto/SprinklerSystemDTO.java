package com.casa.app.device.outdoor.sprinkler_system.dto;

import com.casa.app.device.dto.DeviceDTO;
import com.casa.app.device.DeviceStatus;
import com.casa.app.device.PowerSupplyType;
import com.casa.app.device.outdoor.sprinkler_system.SprinklerSystem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SprinklerSystemDTO extends DeviceDTO {
    public SprinklerSystem toModel() {
        SprinklerSystem device = new SprinklerSystem();
        device.setName(this.getName());
        device.setStatus(DeviceStatus.OFFLINE);
        device.setEnergyConsumption(this.getEnergyConsumption());
        device.setPowerSupplyType(PowerSupplyType.valueOf(this.getPowerSupplyType()));
        return device;
    }
}
