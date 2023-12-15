package com.casa.app.device.large_electric.solar_panel_system.measurement;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.solarPanelSystemCommand)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolarPanelSystemCommand extends AbstractMeasurement {

    @Column(tag = true)
    Long id;

    @Column
    String command;

    @Column
    String user;

    @Column(timestamp = true)
    Instant timestamp;
}