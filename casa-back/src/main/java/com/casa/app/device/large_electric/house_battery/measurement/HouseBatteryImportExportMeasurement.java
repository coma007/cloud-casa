package com.casa.app.device.large_electric.house_battery.measurement;

import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.houseBatteryImportExport)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseBatteryImportExportMeasurement extends AbstractMeasurement {

    @Column(tag = true)
    Long id;

    // add your custom columns
    @Column
    String type;

    @Column
    double value;

    @Column(timestamp = true)
    Instant timestamp;
}
