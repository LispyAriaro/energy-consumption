package com.quick.energy.consumption.conttrollers;

import com.quick.energy.consumption.error.ErrorCode;
import com.quick.energy.consumption.exceptions.ServiceException;
import com.quick.energy.consumption.models.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;


@RestControllerAdvice
public class GeneralControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(GeneralControllerAdvice.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseDto handleMethodNotFound(HttpRequestMethodNotSupportedException ex) {
        return new ResponseDto(ResponseDto.Status.fail, "Method not supported", warn(ex));
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto processServiceException(ServiceException ex) {
        return processServiceException(ex, null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto handleSystemError(Exception ex) {
        return new ResponseDto(ResponseDto.Status.error, "System error. Please contact the administrator.", warn(ex));
    }

    private ResponseDto processServiceException(ServiceException ex, Serializable data) {
        return new ResponseDto(
                ex.getErrorCode() == ErrorCode.SERVER_ERROR
                        ? // Server error?
                        ResponseDto.Status.error : ResponseDto.Status.fail,
                ex.getClientMessage(),
                warn(ex),
                data,
                Optional.ofNullable(ex.getErrorCode()).map(ErrorCode::getCode).orElse(null)
        );
    }

    private String warn(Exception ex) {
        String errorId = UUID.randomUUID().toString().replace("-", "");
        logger.warn(ex.getMessage() + " <errorId: " + errorId + ">", ex);
        return errorId;
    }
}