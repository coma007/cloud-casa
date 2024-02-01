package com.casa.app.device.home.washing_machine.dto;

import com.casa.app.device.home.air_conditioning.AirConditioningMode;
import com.casa.app.device.home.washing_machine.WashingMachineMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WashingMachineSimulationDTO {
    private Long id;
    private List<WashingMachineMode> supportedModes;
}
