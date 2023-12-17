package com.casa.app.device.outdoor.vehicle_gate.dto;

import com.casa.app.device.dto.DeviceDetailsDTO;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGateMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleGateDetailsDTO extends DeviceDetailsDTO {
    private String currentMode;
    private List<String> allowedVehicles;
}
