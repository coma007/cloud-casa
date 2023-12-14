package com.casa.app.influxdb;


import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import io.reactivex.BackpressureOverflowStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class InfluxDBConfig {


    @Value("${influxdb.token}")
    protected String token;

    @Value("${influxdb.bucket}")
    protected String bucket;

    @Value("${influxdb.org}")
    protected String org;

    @Value("${influxdb.url}")
    protected String url;

    @Value("${influxdb.batchSize}")
    protected Integer batchSize;

    @Bean
    public InfluxDBClient buildConnection() {
        return InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
    }

    @Bean
    public WriteApi buildWriteApi(InfluxDBClient client) {
        return client.makeWriteApi(WriteOptions.builder()
                .batchSize(batchSize)
                .flushInterval(1000*60)
                .retryInterval(1000*1)
                .backpressureStrategy(BackpressureOverflowStrategy.DROP_OLDEST)
                .bufferLimit(10000)
                .jitterInterval(1000)
                .build());
    }

}