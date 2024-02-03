package com.casa.app.device.home.air_conditioning.measurements.execution;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.airConditioningExecution)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirConditioningExecution extends AbstractMeasurement {
    @Column(tag = true)
    Long id;

    @Column
    String command;

    @Column
    String executed;

    @Column
    String user;

    @Column(timestamp = true)
    Instant timestamp;


}

