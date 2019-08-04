package com.quick.energy.consumption.services;

import com.quick.energy.consumption.exceptions.DuplicateEntryException;
import com.quick.energy.consumption.models.Counter;
import com.quick.energy.consumption.models.CounterEnergyConsumption;
import com.quick.energy.consumption.models.dto.CounterCreateDto;
import com.quick.energy.consumption.models.dto.CounterEnergyUsageDto;

import java.util.List;

public interface CounterService {
    public void createCounter(CounterCreateDto counterCreateDto) throws DuplicateEntryException;

    public Counter getCounterDetails(String counterId);

    public void saveCounterEnergyUsage(CounterEnergyUsageDto counterEnergyUsageDto);

    public List<CounterEnergyConsumption> getEnergyConsumptionReport();
}
