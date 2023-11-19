package com.casa.app.device.outdoor.vehicle_gate;

import com.casa.app.device.DeviceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleGateDTO extends DeviceDTO {
    private String[] allowedVehicles;
}
