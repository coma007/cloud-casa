package com.casa.app.device.home.air_conditioning;

import com.casa.app.device.DeviceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirConditioningDTO extends DeviceDTO {
    private int minTemperature;
    private int maxTemperature;
    private String[] supportedModes;
}
