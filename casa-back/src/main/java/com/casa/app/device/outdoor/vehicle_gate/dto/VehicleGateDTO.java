package com.casa.app.device.outdoor.vehicle_gate.dto;

import com.casa.app.device.dto.DeviceDTO;
import com.casa.app.device.DeviceStatus;
import com.casa.app.device.PowerSupplyType;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGate;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGateMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleGateDTO extends DeviceDTO {
    private List<String> allowedVehicles;

    public VehicleGate toModel() {
        VehicleGate device = new VehicleGate();
        device.setName(this.getName());
        device.setStatus(DeviceStatus.OFFLINE);
        device.setEnergyConsumption(this.getEnergyConsumption());
        device.setPowerSupplyType(PowerSupplyType.valueOf(this.getPowerSupplyType()));
        device.setAllowedVehicles(new ArrayList<>());
        device.setCurrentMode(VehicleGateMode.PUBLIC);
        for (String vehicle : this.allowedVehicles) {
            device.getAllowedVehicles().add(vehicle);
        }
        return device;
    }
}
