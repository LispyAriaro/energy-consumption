package com.quick.energy.consumption.rest;

import com.quick.energy.consumption.models.CounterEnergyUsageDto;
import com.quick.energy.consumption.models.ResponseDto;
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


    @PostMapping("/counter_callback")
    public ResponseEntity<ResponseDto> approveNewMerchant(@Valid @RequestBody CounterEnergyUsageDto counterEnergyUsageDto, BindingResult fields) {
        RestUtil.validate(fields);

        return RestUtil.response(HttpStatus.CREATED, ResponseDto.Status.success, "Counter measurement accepted!");
    }

    @GetMapping("/consumption_report")
    public ResponseEntity<ResponseDto> consumptionReport(@RequestParam("duration") String duration) {

        return RestUtil.response(HttpStatus.OK, ResponseDto.Status.success, "Energy consumption report");
    }
}
