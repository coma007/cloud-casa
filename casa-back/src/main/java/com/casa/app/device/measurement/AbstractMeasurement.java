package com.casa.app.device.measurement;

import lombok.Data;

import java.time.Instant;

@Data
public abstract class AbstractMeasurement {

    Long id;
    Instant timestamp;
}
