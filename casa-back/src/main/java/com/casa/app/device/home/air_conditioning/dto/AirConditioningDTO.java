package com.casa.app.device.home.air_conditioning.dto;

import com.casa.app.device.dto.DeviceDTO;
import com.casa.app.device.DeviceStatus;
import com.casa.app.device.PowerSupplyType;
import com.casa.app.device.home.air_conditioning.AirConditioning;
import com.casa.app.device.home.air_conditioning.AirConditioningMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirConditioningDTO extends DeviceDTO {
    private int minTemperature;
    private int maxTemperature;
    private List<String> supportedModes;

    public AirConditioning toModel() {
        AirConditioning device = new AirConditioning();
        device.setName(this.getName());
        device.setStatus(DeviceStatus.OFFLINE);
        device.setEnergyConsumption(this.getEnergyConsumption());
        device.setPowerSupplyType(PowerSupplyType.valueOf(this.getPowerSupplyType()));
        device.setMinTemperature(this.minTemperature);
        device.setMaxTemperature(this.maxTemperature);
        device.setSupportedModes(new ArrayList<>());
        for (String mode : this.supportedModes) {
            device.getSupportedModes().add(AirConditioningMode.valueOf(mode));
        }
        return device;
    }
}
