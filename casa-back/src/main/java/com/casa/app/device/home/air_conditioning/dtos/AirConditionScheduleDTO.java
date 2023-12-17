package com.casa.app.device.home.air_conditioning.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirConditionScheduleDTO {
    private String startTime;
    private String endTime;
    private Long deviceId;

    private boolean working;
    private String mode;
    private Double temperature;
}
