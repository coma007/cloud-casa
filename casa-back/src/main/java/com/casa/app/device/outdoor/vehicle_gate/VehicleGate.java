package com.casa.app.device.outdoor;

import com.casa.app.device.Device;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleGate extends Device {
    private VehicleGateMode currentMode;
    private String[] allowedVehicles;
}
