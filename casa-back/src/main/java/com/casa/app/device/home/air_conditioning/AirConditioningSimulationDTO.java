package com.casa.app.device.home.air_conditioning;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirConditioningSimulationDTO {
    private Long id;
    private int minTemperature;
    private int maxTemperature;
    private List<AirConditioningMode> supportedModes;
}
