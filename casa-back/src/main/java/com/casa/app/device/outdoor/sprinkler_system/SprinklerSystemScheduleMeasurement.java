package com.casa.app.device.outdoor.sprinkler_system;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.sprinklerSchedule)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprinklerSystemScheduleMeasurement extends AbstractMeasurement {
    @Column(tag = true)
    Long id;

    @Column
    String user;

    @Column(timestamp = true)
    Instant timestamp;
}
