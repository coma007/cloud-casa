package com.casa.app.device.large_electric.house_battery;

import com.casa.app.device.DeviceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseBatteryDTO extends DeviceDTO {
    private double size;
}
