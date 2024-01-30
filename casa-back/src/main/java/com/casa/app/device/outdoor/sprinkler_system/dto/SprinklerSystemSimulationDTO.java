package com.casa.app.device.outdoor.sprinkler_system.dto;

import com.casa.app.device.outdoor.sprinkler_system.SprinklerSystemSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SprinklerSystemSimulationDTO {
    private Long id;
    private boolean sprinklerOn;
    private boolean forceQuit;
    private SprinklerSystemSchedule schedule;
}
