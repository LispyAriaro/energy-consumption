package com.quick.energy.consumption.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quick.energy.consumption.Constants;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.io.Serializable;
import java.time.Instant;

@Measurement(name = Constants.COUNTERS_MEASUREMENT_NAME)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Counter implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "time")
    @JsonIgnore
    private Instant time;

    @Column(name = "counterId")
    @JsonProperty("id")
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
