package com.casa.app.device.large_electric.house_battery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseBatterySimulationDTO {
    private Long id;
    private double size;
    private double currentState;
}
