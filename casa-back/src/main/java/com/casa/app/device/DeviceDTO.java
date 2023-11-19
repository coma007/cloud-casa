package com.casa.app.device;

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
    private String realEstateName;
}
