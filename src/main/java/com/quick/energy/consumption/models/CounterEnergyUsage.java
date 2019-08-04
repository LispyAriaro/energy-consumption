package com.quick.energy.consumption.models;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.math.BigDecimal;
import java.time.Instant;

@Measurement(name = "energyconsumption")
public class CounterEnergyUsage {
    @Column(name = "time")
    private Instant time;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "counterId")
    private String counterId;

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCounterId() {
        return counterId;
    }

    public void setCounterId(String counterId) {
        this.counterId = counterId;
    }
}
