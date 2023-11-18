package com.casa.app.device.home;

import com.casa.app.device.Device;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirConditioning extends Device {
    private int minTemperature;
    private int maxTemperature;
    private String[] supportedModes;
}
