package com.casa.app.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {
    private String name;
    private String powerSupplyType;
    private double energyConsumption;
    private Long realEstateId;
}
