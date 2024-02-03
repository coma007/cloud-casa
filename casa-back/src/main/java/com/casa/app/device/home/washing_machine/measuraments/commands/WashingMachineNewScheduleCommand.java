package com.casa.app.device.home.washing_machine.measuraments.commands;

import com.casa.app.device.measurement.MeasurementType;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = MeasurementType.washingMachineNewScheduleCommand)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WashingMachineNewScheduleCommand extends WashingMachineCommand {
    @Column(tag = true)
    Long id;

    @Column
    String type;

    @Column
    @JsonRawValue
    String washingMachineSchedule;

    @Column
    String user;

    @Column(timestamp = true)
    Instant timestamp;


    public String toMessage(){
        return type + "|" + washingMachineSchedule + "|" + user;
    }
}
