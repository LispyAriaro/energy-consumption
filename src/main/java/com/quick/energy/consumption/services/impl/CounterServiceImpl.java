package com.quick.energy.consumption.services.impl;

import com.quick.energy.consumption.Constants;
import com.quick.energy.consumption.exceptions.DuplicateEntryException;
import com.quick.energy.consumption.exceptions.InvalidDataFormatException;
import com.quick.energy.consumption.exceptions.NotFoundException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CounterServiceImpl implements CounterService {
    private static final Logger log = LoggerFactory.getLogger(CounterServiceImpl.class);

    @Autowired
    private InfluxDB influxDbConnection;

    @Value("${datasource.dbame}")
    private String dbName;

    @Value("${retention.policy.24hour}")
    private String twentyFourHourRetentionPolicyName;


    @Override
    public void createCounter(CounterCreateDto counterCreateDto) throws DuplicateEntryException {
        Counter counter = null;

        try {
            getCounterDetails(counterCreateDto.getCounterId());
        } catch(NotFoundException ex) {}

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
    public Counter getCounterDetails(String counterId) throws NotFoundException {
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
        } else {
            throw new NotFoundException("Counter not found");
        }
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

    public List<CounterEnergyConsumption> getEnergyConsumptionReport(String hourDurationQueryParam) throws InvalidDataFormatException {
        double hourDuration = getHourDurationParameterAsNumber(hourDurationQueryParam);

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

    private double getHourDurationParameterAsNumber(String hourDurationQueryParam) throws InvalidDataFormatException {
        Pattern pattern = Pattern.compile("^(.+)h$");
        Matcher matcher = pattern.matcher(hourDurationQueryParam);

        String hourDuration = null;
        double hourDurationAsNumber = 0;
        while (matcher.find()) {
            hourDuration = matcher.group(1);
        }
        log.info("hourDurationQueryParam: {}", hourDurationQueryParam);
        log.info("hour duration: {}", hourDuration);

        if(hourDuration == null) {
            throw new InvalidDataFormatException("Duration parameter is not valid");
        } else {
            try {
                return Double.parseDouble(hourDuration);
            } catch(NumberFormatException ex) {
                throw new InvalidDataFormatException("Duration parameter is not valid");
            }
        }
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
