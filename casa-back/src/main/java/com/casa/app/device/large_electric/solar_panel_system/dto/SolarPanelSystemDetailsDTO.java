package com.casa.app.device.large_electric.solar_panel_system.dto;

import com.casa.app.device.dto.DeviceDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolarPanelSystemDetailsDTO extends DeviceDetailsDTO {
    private double size;
    private double efficiency;
}
