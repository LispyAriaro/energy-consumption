package com.quick.energy.consumption.services.impl;

import com.quick.energy.consumption.Constants;
import com.quick.energy.consumption.exceptions.DuplicateEntryException;
import com.quick.energy.consumption.models.Counter;
import com.quick.energy.consumption.models.dto.CounterCreateDto;
import com.quick.energy.consumption.models.dto.CounterEnergyUsageDto;
import com.quick.energy.consumption.services.CounterService;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CounterServiceImpl implements CounterService {
    @Autowired
    private InfluxDB influxDbConnection;

    @Value("${datasource.dbame}")
    private String dbName;

    @Value("${retention.policy.24hour}")
    private String twentyFourHourRetentionPolicyName;


    @Override
    public void createCounter(CounterCreateDto counterCreateDto) throws DuplicateEntryException {
        Counter counter = getCounterDetails(counterCreateDto.getCounterId());

        if(counter != null) {
            throw new DuplicateEntryException("A counter with the same id already exists");
        }

        BatchPoints batchPoints = BatchPoints
                .database(dbName)
                .build();

        Point point = Point.measurement(Constants.COUNTERS_MEASUREMENT_NAME)
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("villageName", counterCreateDto.getVillageName())
                .tag("counterId", counterCreateDto.getCounterId())
                .build();

        batchPoints.point(point);

        influxDbConnection.write(batchPoints);
    }

    @Override
    public Counter getCounterDetails(String counterId) {
        // select query by tag should have the tag single-quoted
        String selectQuery = String.format("select * from %s where counterId='%s'",
                Constants.COUNTERS_MEASUREMENT_NAME, counterId);
        Query query = new Query(selectQuery, dbName);

        QueryResult queryResult = influxDbConnection.query(query);

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<Counter> counters = resultMapper.toPOJO(queryResult, Counter.class);

        if(counters.size() > 0) {
            Counter counter = counters.get(0);
            return counter;
        } else {

        }

        return null;
    }

    @Override
    public void saveCounterEnergyUsage(CounterEnergyUsageDto counterEnergyUsageDto) {
        BatchPoints batchPoints = BatchPoints
                .database(dbName)
                .retentionPolicy(twentyFourHourRetentionPolicyName)
                .build();

        Point point = Point.measurement(Constants.ENERGY_CONSUMPTION_MEASUREMENT_NAME)
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("amount", counterEnergyUsageDto.getAmount())
                .tag("counterId", counterEnergyUsageDto.getCounterId())
                .build();

        batchPoints.point(point);

        influxDbConnection.write(batchPoints);
    }
}
