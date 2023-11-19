package com.casa.app.device.home.washing_machine;

import com.casa.app.device.DeviceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WashingMachineDTO extends DeviceDTO {
    private String[] supportedModes;
}
