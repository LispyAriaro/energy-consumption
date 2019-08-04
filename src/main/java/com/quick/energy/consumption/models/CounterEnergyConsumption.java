package com.quick.energy.consumption.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quick.energy.consumption.Constants;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.math.BigDecimal;
import java.time.Instant;

@Measurement(name = Constants.ENERGY_CONSUMPTION_MEASUREMENT_NAME)
public class CounterEnergyConsumption {
    @Column(name = "time")
    @JsonIgnore
    private Instant time;

    @Column(name = "amount")
    @JsonProperty("consumption")
    private BigDecimal amount;

    @Column(name = "counterId")
    @JsonIgnore
    private String counterId;

    @JsonProperty("village_name")
    private String villageName;

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

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }
}
