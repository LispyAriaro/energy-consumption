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

    @Value("${datasource.dbname.villages}")
    private String villagesDatabaseName;

    @Value("${datasource.dbame.village.consumptions}")
    private String villageConsumptionsDatabaseName;

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
        } else {
            if(!influxDB.databaseExists(villagesDatabaseName)) {
                influxDB.createDatabase(villagesDatabaseName);
                // assumes default retention policy(RP) "autogen".
                // The autogen RP has an infinite retention period
            }
            if(!influxDB.databaseExists(villageConsumptionsDatabaseName)) {
                influxDB.createDatabase(villageConsumptionsDatabaseName);
                influxDB.createRetentionPolicy("defaultPolicy", villageConsumptionsDatabaseName, "24h", 1, true);
            }
        }
        influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);

        return influxDB;
    }
}
