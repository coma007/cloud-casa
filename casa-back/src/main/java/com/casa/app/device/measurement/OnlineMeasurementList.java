package com.casa.app.device.measurement;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class OnlineMeasurementList {
    private Long deviceId;
    private Instant from;
    private Instant to;
    private Map<String, String> counts;

    private Boolean hourly;
    private Long maxCount;
    private Long totalMaxCount;
    private Long delay;
}
