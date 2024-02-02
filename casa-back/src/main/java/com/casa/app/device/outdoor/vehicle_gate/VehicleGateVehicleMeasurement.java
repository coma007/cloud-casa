package com.casa.app.device.outdoor.vehicle_gate;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.vehicleGateVehicles)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleGateVehicleMeasurement extends AbstractMeasurement {
    @Column(tag = true)
    Long id;

    @Column
    Boolean adding;

    @Column
    String vehicle;

    @Column
    String user;

    @Column(timestamp = true)
    Instant timestamp;
}
