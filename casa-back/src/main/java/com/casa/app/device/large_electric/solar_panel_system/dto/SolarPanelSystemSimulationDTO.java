package com.casa.app.device.large_electric.solar_panel_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolarPanelSystemSimulationDTO {

    private Long id;
    private double size;
    private double efficiency;

}
