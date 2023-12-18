package com.casa.app.device.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDetailsDTO{

    private Long id;
    private String type;
    private String status;
    private String name;
    private String powerSupplyType;
    private double energyConsumption;
    private String realEstateName;
}
