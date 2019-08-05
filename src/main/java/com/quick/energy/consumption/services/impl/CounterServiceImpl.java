package com.quick.energy.consumption.services.impl;

import com.quick.energy.consumption.Constants;
import com.quick.energy.consumption.exceptions.DuplicateEntryException;
import com.quick.energy.consumption.models.Counter;
import com.quick.energy.consumption.models.CounterEnergyConsumption;
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

import java.util.ArrayList;
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
        // select query by tag in where clause must have the tag value single-quoted
        String selectQuery = String.format("select * from %s where counterId = '%s' ORDER BY time DESC LIMIT 1",
                Constants.COUNTERS_MEASUREMENT_NAME, counterId);
        Query query = new Query(selectQuery, dbName);

        QueryResult queryResult = influxDbConnection.query(query);

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<Counter> counters = resultMapper.toPOJO(queryResult, Counter.class);

        if(counters.size() > 0) {
            Counter counter = counters.get(0);
            return counter;
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

    public List<CounterEnergyConsumption> getEnergyConsumptionReport() {
        String selectQuery = String.format("select * from %s", Constants.COUNTERS_MEASUREMENT_NAME);
        Query query = new Query(selectQuery, dbName);

        QueryResult queryResult = influxDbConnection.query(query);

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<Counter> counters = resultMapper.toPOJO(queryResult, Counter.class);
        List<CounterEnergyConsumption> reportData = new ArrayList<>();

        for(Counter counter : counters) {
            CounterEnergyConsumption consumption = getVillageConsumption(counter);
            reportData.add(consumption);
        }

        return reportData;
    }

    private CounterEnergyConsumption getVillageConsumption(Counter counter) {
        String qualifedMeasurement = String.format("\"%s\".\"%s\".\"%s\"", dbName, twentyFourHourRetentionPolicyName,
                Constants.ENERGY_CONSUMPTION_MEASUREMENT_NAME);
        String selectLastPointQuery = String.format("select * from %s where counterId = '%s' ORDER BY time DESC LIMIT 1",
                qualifedMeasurement, counter.getCounterId());

        String selectFirstPointQuery = String.format("select * from %s where counterId = '%s' ORDER BY time ASC LIMIT 1",
                qualifedMeasurement, counter.getCounterId());

        QueryResult lastPointQueryResult = influxDbConnection.query(new Query(selectLastPointQuery, dbName));

        QueryResult firstPointQueryResult = influxDbConnection.query(new Query(selectFirstPointQuery, dbName));

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<CounterEnergyConsumption> lastPoint = resultMapper.toPOJO(lastPointQueryResult,
                CounterEnergyConsumption.class);
        CounterEnergyConsumption lastEnergyConsumption = null;
        if(lastPoint.size() > 0) {
            lastEnergyConsumption = lastPoint.get(0);
        }

        List<CounterEnergyConsumption> firstPoint = resultMapper.toPOJO(firstPointQueryResult,
                CounterEnergyConsumption.class);
        CounterEnergyConsumption firstEnergyConsumption = null;
        if(firstPoint.size() > 0) {
            firstEnergyConsumption = firstPoint.get(0);
        }

        if(lastEnergyConsumption == null) {
            CounterEnergyConsumption result = new CounterEnergyConsumption();
            result.setAmount(0);
            result.setCounterId(counter.getCounterId());
            result.setVillageName(counter.getVillageName());
            return result;
        } else {
            double energyUsed = lastEnergyConsumption.getAmount() - firstEnergyConsumption.getAmount();
            if(energyUsed == 0) {
                energyUsed = lastEnergyConsumption.getAmount();
            }
            CounterEnergyConsumption result = new CounterEnergyConsumption();
            result.setAmount(energyUsed);
            result.setCounterId(counter.getCounterId());
            result.setVillageName(counter.getVillageName());
            return result;
        }
    }
}
