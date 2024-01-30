package com.casa.app.device.outdoor.sprinkler_system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SprinklerSystemSchedule {
    private Instant startTime;
    private Instant endTime;
    private boolean[] scheduledDays;
}
