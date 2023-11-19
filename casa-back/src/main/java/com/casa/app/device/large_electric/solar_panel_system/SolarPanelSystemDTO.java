package com.casa.app.device.large_electric.solar_panel_system;

import com.casa.app.device.DeviceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolarPanelSystemDTO extends DeviceDTO {
    private double size;
    private double efficiency;
}
