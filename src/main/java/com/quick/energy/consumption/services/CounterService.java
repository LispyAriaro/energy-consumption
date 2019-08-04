package com.quick.energy.consumption.services;

import com.quick.energy.consumption.models.CounterEnergyUsageDto;

public interface CounterService {
    public void saveCounterEnergyUsage(CounterEnergyUsageDto counterEnergyUsageDto);
}
