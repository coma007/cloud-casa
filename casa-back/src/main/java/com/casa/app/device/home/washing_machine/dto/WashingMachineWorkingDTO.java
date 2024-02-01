package com.casa.app.device.home.washing_machine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WashingMachineWorkingDTO {

    private boolean working;
    private Long id;
}
