package com.casa.app.device.outdoor.lamp;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.lampBrightness)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LampBrightnessMeasurement extends AbstractMeasurement {

    @Column(tag = true)
    Long id;

    @Column
    Double brightness;

    @Column(timestamp = true)
    Instant timestamp;
}
