package com.casa.app.device.home.air_conditioning.measurements.commands;

import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.airConditioningTemperatureCommand)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirConditioningTemperatureCommand extends AirConditionCommand {
    @Column(tag = true)
    Long id;

    @Column
    String type;

    @Column
    double temperature;

    @Column
    String user;

    @Column(timestamp = true)
    Instant timestamp;


    @Override
    public String toMessage(){
        return type + "|" + temperature + "|" + user;
    }
}
