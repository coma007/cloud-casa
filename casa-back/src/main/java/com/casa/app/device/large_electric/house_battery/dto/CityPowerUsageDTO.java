package com.casa.app.device.large_electric.house_battery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityPowerUsageDTO {
    private String cityName;
    private double powerUsage;
    private double powerProduction;
}