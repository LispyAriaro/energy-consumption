package com.quick.energy.consumption.conttrollers;

import com.quick.energy.consumption.models.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;


@RestControllerAdvice
public class GeneralControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(GeneralControllerAdvice.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseDto handleMethodNotFound(HttpRequestMethodNotSupportedException ex) {
        return new ResponseDto(ResponseDto.Status.fail, "Method not supported", warn(ex));
    }

    private String warn(Exception ex) {
        String errorId = UUID.randomUUID().toString().replace("-", "");
        logger.warn(ex.getMessage() + " <errorId: " + errorId + ">", ex);
        return errorId;
    }
}