package com.casa.app.device.large_electric.electric_vehicle_charger;

import com.casa.app.device.Device;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class ElectricVehicleCharger extends Device {
    private int chargePower;
    private int numOfSlots;
}
