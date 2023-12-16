package com.casa.app.device.home.air_conditioning.measurements.commands;

import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.airConditioningWorkingCommand)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirConditioningWorkingCommand extends AirConditionCommand {
    @Column(tag = true)
    Long id;

    @Column
    String type;

    @Column
    String working;


    @Column
    String user;

    @Column(timestamp = true)
    Instant timestamp;


    public String toMessage(){
        return type + "|" + working + "|" + user;
    }
}
