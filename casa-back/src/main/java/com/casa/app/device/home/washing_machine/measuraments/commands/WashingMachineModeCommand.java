package com.casa.app.device.home.washing_machine.measuraments.commands;

import com.casa.app.device.home.air_conditioning.measurements.commands.AirConditionCommand;
import com.casa.app.device.measurement.MeasurementType;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.washingMachineModeCommand)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WashingMachineModeCommand extends WashingMachineCommand {
    @Column(tag = true)
    Long id;

    @Column
    String type;

    @Column
    String mode;

    @Column
    String user;

    @Column(timestamp = true)
    Instant timestamp;

    @Override
    public String toMessage(){
        return type + "|" + mode + "|" + user;
    }
}
