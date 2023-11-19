package com.casa.app.device.large_electric.solar_panel_system;

import com.casa.app.device.DeviceDTO;
import com.casa.app.device.DeviceStatus;
import com.casa.app.device.PowerSupplyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolarPanelSystemDTO extends DeviceDTO {
    private double size;
    private double efficiency;

    public SolarPanelSystem toModel() {
        SolarPanelSystem device = new SolarPanelSystem();
        device.setName(this.getName());
        device.setStatus(DeviceStatus.OFFLINE);
        device.setEnergyConsumption(this.getEnergyConsumption());
        device.setPowerSupplyType(PowerSupplyType.valueOf(this.getPowerSupplyType()));
        device.setSize(this.size);
        device.setEfficiency(this.efficiency);
        return device;
    }
}
