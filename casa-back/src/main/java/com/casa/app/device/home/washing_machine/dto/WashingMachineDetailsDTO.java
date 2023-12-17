package com.casa.app.device.home.washing_machine.dto;

import com.casa.app.device.dto.DeviceDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WashingMachineDetailsDTO extends DeviceDetailsDTO {

    private List<String> supportedModes;

}
