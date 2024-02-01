package com.casa.app.device.outdoor.sprinkler_system;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.sprinklerCommand)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprinklerSystemCommandMeasurement extends AbstractMeasurement {
    @Column(tag = true)
    Long id;

    @Column
    Boolean is_on;

    @Column
    Boolean is_schedule;

    @Column
    String user;

    @Column(timestamp = true)
    Instant timestamp;
}
