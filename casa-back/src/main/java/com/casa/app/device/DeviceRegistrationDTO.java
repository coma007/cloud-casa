package com.casa.app.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRegistrationDTO<T extends DeviceDTO> {
    private String type;
    private T deviceDTO;
}
