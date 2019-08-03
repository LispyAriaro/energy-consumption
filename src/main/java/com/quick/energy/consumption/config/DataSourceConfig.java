package com.quick.energy.consumption.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${datasource.url}")
    private String databaseURL;

    @Value("${datasource.username}")
    private String databaseUsername;

    @Value("${datasource.password}")
    private String databasePassword;

    @Bean
    @Primary
    public InfluxDB dataSource() {
        InfluxDB influxDB = InfluxDBFactory.connect(databaseURL, databaseUsername, databasePassword);

        Pong influxDbPingResponse = influxDB.ping();
        if (influxDbPingResponse.getVersion().equalsIgnoreCase("unknown")) {
            return null;
        }
        return influxDB;
    }
}
