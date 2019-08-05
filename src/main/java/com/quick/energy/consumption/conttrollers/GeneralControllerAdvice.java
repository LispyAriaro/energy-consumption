package com.quick.energy.consumption.conttrollers;

import com.quick.energy.consumption.error.ErrorCode;
import com.quick.energy.consumption.exceptions.DuplicateEntryException;
import com.quick.energy.consumption.exceptions.InvalidDataFormatException;
import com.quick.energy.consumption.exceptions.NotFoundException;
import com.quick.energy.consumption.exceptions.ServiceException;
import com.quick.energy.consumption.models.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.Serializable;
import java.util.Optional;


@RestControllerAdvice
public class GeneralControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(GeneralControllerAdvice.class);

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDto handleEntityNotException(NotFoundException ex) {
        return new ResponseDto(ResponseDto.Status.fail, ex.getMessage());
    }

    @ExceptionHandler(InvalidDataFormatException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseDto handleInvalidDataFormatException(InvalidDataFormatException ex) {
        return new ResponseDto(ResponseDto.Status.fail, ex.getMessage());
    }

    @ExceptionHandler(DuplicateEntryException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseDto handleGeneralDuplicateException(DuplicateEntryException ex) {
        logger.error(ex.getMessage(), ex);

        return new ResponseDto(ResponseDto.Status.fail, ex.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto processServiceException(ServiceException ex) {
        logger.error(ex.getMessage(), ex);

        return processServiceException(ex, null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto handleSystemError(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return new ResponseDto(ResponseDto.Status.error, "System error. Please contact the administrator.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseDto handleMethodNotFound(HttpRequestMethodNotSupportedException ex) {
        return new ResponseDto(ResponseDto.Status.fail, "Method not supported");
    }

    private ResponseDto processServiceException(ServiceException ex, Serializable data) {
        return new ResponseDto(
                ex.getErrorCode() == ErrorCode.SERVER_ERROR
                        ? // Server error?
                        ResponseDto.Status.error : ResponseDto.Status.fail,
                ex.getClientMessage(), data, Optional.ofNullable(ex.getErrorCode()).map(ErrorCode::getCode).orElse(null)
        );
    }
}