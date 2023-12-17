package com.casa.app.device.home.air_conditioning.schedule;

import com.casa.app.device.Device;
import com.casa.app.device.home.air_conditioning.AirConditioning;
import com.casa.app.device.home.air_conditioning.AirConditioningMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AirConditionSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean override;
    private boolean activated;

    @ManyToOne
    private AirConditioning airConditioning;

    private boolean working;
    private String mode;
    private Double temperature;

    private boolean repeating;
    private Long repeatingDaysIncrement;

}

