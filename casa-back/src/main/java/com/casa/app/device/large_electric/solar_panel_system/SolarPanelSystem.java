package com.casa.app.device.large_electric.solar_panel_system;

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
public class SolarPanelSystem extends Device {
    private double size;
    private double efficiency;

}
