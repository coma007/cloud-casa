package com.casa.app.influxdb;

import com.casa.app.device.Device;
import com.casa.app.device.measurement.*;
import com.casa.app.user.User;
import com.influxdb.client.*;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InfluxDBService {

    @Autowired
    private InfluxDBConfig config;

    @Autowired
    private InfluxDBClient client;

    @Autowired
    private WriteApi writeApi;

    @Autowired
    private MeasurementService measurementService;



    public void write(AbstractMeasurement measurement) {
        measurement.setTimestamp(Instant.now());
        try {
            writeApi.writeMeasurement(WritePrecision.MS, measurement);
        } catch (InfluxException e) {
            throw new RuntimeException(e);
        }
    }


    public MeasurementList query(String measurement, Device device, Instant from, Instant to, User user, boolean findUser) {

        if (from == null || to == null) {
            to = Instant.now();
            from = to.minus(24, ChronoUnit.HOURS);
        }
        else if (Duration.between(from, to).toDays() > 30) {
            from = to.minus(1, ChronoUnit.MONTHS);
        }
        String fromString = from.toString();
        String toString = to.toString();

        String flux = buildFluxQuery(measurement, device, user, fromString, toString, findUser);
        QueryApi queryApi = client.getQueryApi();

        List<FluxTable> tables = queryApi.query(flux);
        List<FluxRecord> records = new ArrayList<>();
        if (tables.size() > 0) {
            records = tables.get(0).getRecords();
        }

        MeasurementList measurements = new MeasurementList(measurement, device.getId(), from, to);
        for (FluxRecord fluxRecord : records) {
            measurements.add(measurementService.createMeasurement(fluxRecord));
        }

        return measurements;
    }

    public OnlineMeasurementList queryActivity(Device device, Instant from, Instant to) {

        if (from == null || to == null) {
            to = Instant.now();
            from = to.minus(24, ChronoUnit.HOURS);
        }
        else if (Duration.between(from, to).toDays() > 30) {
            from = to.minus(1, ChronoUnit.MONTHS);
        }
        String fromString = from.toString();
        String toString = to.toString();

        boolean hourly = true;
        if (Duration.between(from, to).toDays() > 2) {
            hourly = false;
        }

        String flux = buildFluxActivityQuery(device, fromString, toString, hourly);
        QueryApi queryApi = client.getQueryApi();

        List<FluxTable> tables = queryApi.query(flux);

        HashMap<String, String> counts = tables.stream()
                .flatMap(table -> table.getRecords().stream())  // Flatten the records
                .map(FluxRecord::getValues)
                .map(values -> {
                    String timeValue = String.valueOf(values.get("_time"));
                    String valueValue = String.valueOf(values.get("_value"));
                    return Map.entry(timeValue, valueValue);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (existing, replacement) -> existing, HashMap::new));

        Long maxCount = (60L / 15L) * 60L;
        if (!hourly) {
            maxCount *= 24L;
        }
        return new OnlineMeasurementList(device.getId(), from, to, new TreeMap<>(counts), hourly, maxCount, 15L);
    }

    private String buildFluxQuery(String measurement, Device device, User user, String fromString, String toString, boolean findUser) {
        String flux = String.format(
                "from(bucket:\"%s\") " +
                        "|> range(start: %s, stop: %s)" +
                        "|> filter(fn: (r) => r[\"_measurement\"] == \"%s\" and r[\"id\"] == \"%s\")   " +
                        "|> sort(columns: [\"_time\"]) "  +
                        "|> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\") %s",
                config.bucket,
                fromString, toString,
                measurement, device.getId(),
                findUser ? (user != null ? String.format("|> filter(fn: (r) => r[\"user\"] == \"%s\")", user.getUsername()) : String.format("|> filter(fn: (r) => r[\"user\"] == \"%s\")", "")) : "");
        return flux;
    }

    private String buildFluxActivityQuery(Device device,String fromString, String toString, boolean hourly) {
        String flux = String.format(
                "import \"date\"" +
                        "from(bucket:\"%s\") " +
                        "|> range(start: %s, stop: %s)" +
                        "|> filter(fn: (r) => r[\"_measurement\"] == \"online\" and r[\"id\"] == \"%s\" and r._field == \"is_online\")  " +
                        "|> aggregateWindow(every: 1%s, fn: count, createEmpty: true)" +
                        "|> map(fn: (r) => ({ r with _time: date.truncate(t: r._time, unit: 1%s) }))" +
                        "|> sort(columns: [\"_time\"])",
                config.bucket,
                fromString, toString,
                device.getId(),
                hourly ? "h" : "d",
                hourly ? "h" : "d");
        return flux;
    }



}
