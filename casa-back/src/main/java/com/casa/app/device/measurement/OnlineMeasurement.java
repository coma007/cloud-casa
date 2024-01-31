package com.casa.app.device.measurement;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.online)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineMeasurement extends AbstractMeasurement {

    @Column(tag = true)
    Long id;

    @Column
    Boolean is_online;

    @Column(timestamp = true)
    Instant timestamp;
}