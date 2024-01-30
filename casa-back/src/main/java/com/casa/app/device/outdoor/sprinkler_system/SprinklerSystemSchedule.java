package com.casa.app.device.outdoor.sprinkler_system;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SprinklerSystemSchedule {

    private Instant startTime;
    private Instant endTime;
    private boolean[] scheduledDays;

}
