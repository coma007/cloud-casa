package com.casa.app.device.large_electric.electric_vehicle_charger.measurement;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.electricVehicleChargerCommand)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricVehicleChargerCommandMeasurement extends AbstractMeasurement {

    @Column(tag = true)
    Long id;

    @Column
    String user;

    @Column
    String command;

    // add your custom columns

    @Column(timestamp = true)
    Instant timestamp;
}
