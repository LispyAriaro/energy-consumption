package com.quick.energy.consumption.services;

import com.quick.energy.consumption.exceptions.DuplicateEntryException;
import com.quick.energy.consumption.exceptions.InvalidDataFormatException;
import com.quick.energy.consumption.exceptions.NotFoundException;
import com.quick.energy.consumption.models.Counter;
import com.quick.energy.consumption.models.dto.CounterCreateDto;
import com.quick.energy.consumption.models.dto.CounterEnergyReportDto;
import com.quick.energy.consumption.models.dto.CounterEnergyUsageDto;

import java.util.List;

public interface CounterService {
    public void createCounter(CounterCreateDto counterCreateDto) throws DuplicateEntryException;

    public Counter getCounterDetails(String counterId) throws NotFoundException;

    public void saveCounterEnergyUsage(CounterEnergyUsageDto counterEnergyUsageDto) throws NotFoundException;

    public List<CounterEnergyReportDto> getEnergyConsumptionReport(String duration) throws InvalidDataFormatException;
}
