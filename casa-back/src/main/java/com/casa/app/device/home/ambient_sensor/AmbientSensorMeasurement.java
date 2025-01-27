package com.casa.app.device.home.ambient_sensor;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.ambientSensor)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbientSensorMeasurement extends AbstractMeasurement {
    @Column(tag = true)
    Long id;

    @Column
    double temperature;

    @Column
    double humidity;

    @Column(timestamp = true)
    Instant timestamp;
}
