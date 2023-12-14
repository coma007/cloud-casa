package com.casa.app.device.outdoor.lamp;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.lamp)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LampMeasurement extends AbstractMeasurement {

    @Column(tag = true)
    String id;

    @Column
    Double brightness;

    @Column
    Boolean is_on;

    @Column(timestamp = true)
    Instant timestamp;
}
