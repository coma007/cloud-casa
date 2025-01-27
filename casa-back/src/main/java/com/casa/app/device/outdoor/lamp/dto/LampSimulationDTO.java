package com.casa.app.device.outdoor.lamp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LampSimulationDTO {
    private Long id;
    private boolean lampOn;
}
