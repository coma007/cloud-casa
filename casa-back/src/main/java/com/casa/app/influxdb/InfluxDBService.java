package com.casa.app.influxdb;

import com.casa.app.device.Device;
import com.casa.app.device.measurement.*;
import com.casa.app.user.regular_user.RegularUser;
import com.influxdb.client.*;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import io.reactivex.BackpressureOverflowStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
        try {
            writeApi.writeMeasurement(WritePrecision.MS, measurement);
        } catch (InfluxException e) {
            throw new RuntimeException(e);
        }
    }


    public MeasurementList query(String measurement, Device device, Instant from, Instant to, RegularUser user) {

        if (from == null || to == null) {
            to = Instant.now();
            from = to.minus(24, ChronoUnit.HOURS);
        }
        else if (Duration.between(from, to).toDays() > 30) {
            from = to.minus(1, ChronoUnit.MONTHS);
        }
        String fromString = from.toString();
        String toString = to.toString();

        String flux = buildFluxQuery(measurement, device, user, fromString, toString);
        QueryApi queryApi = client.getQueryApi();

        FluxTable table = queryApi.query(flux).get(0);
        List<FluxRecord> records = table.getRecords();

        MeasurementList measurements = new MeasurementList(measurement, device.getId(), from, to);
        for (FluxRecord fluxRecord : records) {
            measurements.add(measurementService.createMeasurement(fluxRecord));
        }

        return measurements;
    }

    private String buildFluxQuery(String measurement, Device device, RegularUser user, String fromString, String toString) {
        String flux = String.format(
                "from(bucket:\"%s\") " +
                        "|> range(start: %s, stop: %s)" +
                        "|> filter(fn: (r) => r[\"_measurement\"] == \"%s\" and r[\"id\"] == \"%s\" %s)   " +
                        "|> sort(columns: [\"_time\"]) "  +
                        "|> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")",
                config.bucket,
                fromString, toString,
                measurement, device.getId(),
                user != null ? String.format(" and r[\"user_id\"] == \"%s\"", user.getId()) : "");
        return flux;
    }


}
