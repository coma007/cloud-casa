package com.casa.app.device.outdoor.vehicle_gate;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.vehicleGateLicencePlates)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleGateLicencePlatesMeasurement extends AbstractMeasurement {
    @Column(tag = true)
    Long id;

    @Column
    String licence_plates;

    @Column(timestamp = true)
    Instant timestamp;
}
