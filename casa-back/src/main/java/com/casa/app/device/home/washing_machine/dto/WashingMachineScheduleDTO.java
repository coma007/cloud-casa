package com.casa.app.device.home.washing_machine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WashingMachineScheduleDTO {
    private String startTime;
    private String endTime;
    private Long deviceId;

    private boolean working;
    private String mode;

}
