package com.casa.app.device.large_electric.house_battery.dto;

import com.casa.app.device.dto.DeviceDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseBatteryDetailsDTO extends DeviceDetailsDTO {
    private double size;
}
