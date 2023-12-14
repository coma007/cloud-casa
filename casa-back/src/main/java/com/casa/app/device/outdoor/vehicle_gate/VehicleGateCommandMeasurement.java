package com.casa.app.device.outdoor.vehicle_gate;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.vehicleGateCommand)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleGateCommandMeasurement extends AbstractMeasurement {
    @Column(tag = true)
    String id;

    @Column
    Boolean is_open;

    @Column(timestamp = true)
    Instant timestamp;
}
