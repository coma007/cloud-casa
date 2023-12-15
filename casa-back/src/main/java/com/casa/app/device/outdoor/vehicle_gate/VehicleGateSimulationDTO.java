package com.casa.app.device.outdoor.vehicle_gate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleGateSimulationDTO {
    private Long id;
    private List<String> allowedVehicles;
    private VehicleGateMode currentMode;
}
