package com.quick.energy.consumption.services.impl;

import com.quick.energy.consumption.models.CounterEnergyUsageDto;
import com.quick.energy.consumption.services.CounterService;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CounterServiceImpl implements CounterService {
    @Autowired
    private InfluxDB influxDbConnection;

    @Value("${datasource.dbame.village.consumptions}")
    private String villageConsumptionsDatabaseName;


    @Override
    public void saveCounterEnergyUsage(CounterEnergyUsageDto counterEnergyUsageDto) {
        BatchPoints batchPoints = BatchPoints
                .database(villageConsumptionsDatabaseName)
                .retentionPolicy("defaultPolicy")
                .build();

        Point point = Point.measurement("energyconsumption")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("amount", counterEnergyUsageDto.getAmount())
                .tag("counterId", counterEnergyUsageDto.getCounterId())
                .build();

        batchPoints.point(point);

        influxDbConnection.write(batchPoints);
    }
}
