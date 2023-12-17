package com.casa.app.device.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDetailsDTO extends DeviceDTO{

    private Long id;
    private String type;
    private String status;
}
