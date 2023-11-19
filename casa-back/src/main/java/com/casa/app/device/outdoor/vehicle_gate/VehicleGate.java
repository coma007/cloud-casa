package com.casa.app.device.outdoor.vehicle_gate;

import com.casa.app.device.Device;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class VehicleGate extends Device {
    private VehicleGateMode currentMode;
    private List<String> allowedVehicles;
}
