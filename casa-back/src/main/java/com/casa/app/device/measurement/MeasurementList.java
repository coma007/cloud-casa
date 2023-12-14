package com.casa.app.device.measurement;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class MeasurementList {

    private String deviceType;
    private Long deviceId;
    private Instant from;
    private Instant to;
    private List<AbstractMeasurement> measurements;

    public MeasurementList(String deviceType, Long deviceId, Instant from, Instant to) {
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.measurements = new ArrayList<>();
        this.from = from;
        this.to = to;
    }

    public void add(AbstractMeasurement measurement) {
        measurements.add(measurement);
    }

}
