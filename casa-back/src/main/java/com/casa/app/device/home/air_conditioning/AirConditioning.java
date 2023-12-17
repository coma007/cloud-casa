package com.casa.app.device.home.air_conditioning;

import com.casa.app.device.Device;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class AirConditioning extends Device {
    private int minTemperature;
    private int maxTemperature;

    private List<AirConditioningMode> supportedModes;

//    private Double currentTargetTemperature;
//    private Boolean working;
//    private String mode;
}
