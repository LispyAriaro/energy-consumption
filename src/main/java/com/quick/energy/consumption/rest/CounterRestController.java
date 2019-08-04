package com.quick.energy.consumption.rest;

import com.quick.energy.consumption.exceptions.DuplicateEntryException;
import com.quick.energy.consumption.models.Counter;
import com.quick.energy.consumption.models.CounterEnergyConsumption;
import com.quick.energy.consumption.models.dto.CounterCreateDto;
import com.quick.energy.consumption.models.dto.CounterEnergyUsageDto;
import com.quick.energy.consumption.models.dto.ResponseDto;
import com.quick.energy.consumption.services.CounterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * @author efe ariaroo
 */
@RestController
@RequestMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class CounterRestController {
    private static final Logger log = LoggerFactory.getLogger(CounterRestController.class);

    @Autowired
    private CounterService counterService;


    @PostMapping("/counter_create")
    public ResponseEntity<ResponseDto> counterCreate(@Valid @RequestBody CounterCreateDto counterCreateDto, BindingResult fields) throws DuplicateEntryException {
        RestUtil.validate(fields);

        counterService.createCounter(counterCreateDto);

        return RestUtil.response(HttpStatus.CREATED, ResponseDto.Status.success, "Counter created successfully!");
    }

    @GetMapping("/counter")
    public ResponseEntity<Serializable> counterDetails(@RequestParam("id") String counterId) {

        Counter counter = counterService.getCounterDetails(counterId);

        return RestUtil.respondWithPlainPojo(HttpStatus.OK, ResponseDto.Status.success, counter);
    }

    @PostMapping("/counter_callback")
    public ResponseEntity<ResponseDto> counterCallback(@Valid @RequestBody CounterEnergyUsageDto counterEnergyUsageDto, BindingResult fields) {
        RestUtil.validate(fields);

        counterService.saveCounterEnergyUsage(counterEnergyUsageDto);

        return RestUtil.response(HttpStatus.CREATED, ResponseDto.Status.success, "Counter measurement accepted!");
    }

    @GetMapping("/consumption_report")
    public ResponseEntity<ResponseDto> consumptionReport(@RequestParam("duration") String duration) {
        List<CounterEnergyConsumption> reportData = counterService.getEnergyConsumptionReport();

        return RestUtil.response(HttpStatus.OK, ResponseDto.Status.success, "Energy consumption report", reportData);
    }
}
