package com.casa.app.device.home.washing_machine.schedule;

import com.casa.app.device.home.air_conditioning.AirConditioning;
import com.casa.app.device.home.washing_machine.WashingMachine;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class WashingMachineSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean activated;


    @ManyToOne
    @JsonIgnore
    private WashingMachine washingMachine;

    private boolean working;
    private String mode;

}
