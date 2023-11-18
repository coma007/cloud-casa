package com.casa.app.device.large_electric;

import com.casa.app.device.Device;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricVehicleCharger extends Device {
    private int chargePower;
    private int numOfSlots;
}
