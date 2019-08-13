package com.quick.energy.consumption.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quick.energy.consumption.Constants;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = Constants.ENERGY_CONSUMPTION_MEASUREMENT_NAME)
public class CounterEnergyReportDto {
    @Column(name = "time")
    @JsonIgnore
    private Instant time;

    @Column(name = "amount")
    @JsonProperty("consumption")
    private double amount;

    @Column(name = "counterId")
    @JsonIgnore
    private String counterId;

    @Column(name = "villageName")
    @JsonProperty("village_name")
    private String villageName;

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCounterId() {
        return counterId;
    }

    public void setCounterId(String counterId) {
        this.counterId = counterId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }
}
