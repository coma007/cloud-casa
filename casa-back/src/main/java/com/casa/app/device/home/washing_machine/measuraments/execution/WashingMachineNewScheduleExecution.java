package com.casa.app.device.home.washing_machine.measuraments.execution;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.washingMachineNewScheduleAck)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WashingMachineNewScheduleExecution extends AbstractMeasurement {
    @Column(tag = true)
    Long id;

    @Column
    String mode;

    @Column
    String executed;

    @Column
    String user;

    @Column(timestamp = true)
    Instant timestamp;



}
