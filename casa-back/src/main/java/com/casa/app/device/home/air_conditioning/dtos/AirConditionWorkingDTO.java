package com.casa.app.device.home.air_conditioning.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirConditionWorkingDTO {

    private boolean working;
    private Long id;
}
