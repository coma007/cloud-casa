package com.casa.app.device.home.air_conditioning;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.airConditioning)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirConditioningMeasurement extends AbstractMeasurement {
    @Column(tag = true)
    String id;

    // add your custom columns

    @Column(timestamp = true)
    Instant timestamp;
}
