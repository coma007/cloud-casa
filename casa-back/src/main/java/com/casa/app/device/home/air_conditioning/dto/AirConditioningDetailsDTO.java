package com.casa.app.device.home.air_conditioning.dto;

import com.casa.app.device.dto.DeviceDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirConditioningDetailsDTO extends DeviceDetailsDTO {
    private int minTemperature;
    private int maxTemperature;
    private List<String> supportedModes;

}
