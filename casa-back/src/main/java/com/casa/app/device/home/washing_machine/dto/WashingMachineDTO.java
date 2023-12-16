package com.casa.app.device.home.washing_machine.dto;

import com.casa.app.device.dto.DeviceDTO;
import com.casa.app.device.DeviceStatus;
import com.casa.app.device.PowerSupplyType;
import com.casa.app.device.home.washing_machine.WashingMachine;
import com.casa.app.device.home.washing_machine.WashingMachineMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WashingMachineDTO extends DeviceDTO {
    private List<String> supportedModes;

    public WashingMachine toModel() {
        WashingMachine device = new WashingMachine();
        device.setName(this.getName());
        device.setStatus(DeviceStatus.OFFLINE);
        device.setEnergyConsumption(this.getEnergyConsumption());
        device.setPowerSupplyType(PowerSupplyType.valueOf(this.getPowerSupplyType()));
        device.setSupportedMods(new ArrayList<>());
        for (String mod: this.supportedModes) {
            device.getSupportedMods().add(WashingMachineMode.valueOf(mod));
        }
        return device;
    }
}
