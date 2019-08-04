package com.quick.energy.consumption.rest;

import com.quick.energy.consumption.exceptions.ServiceException;
import com.quick.energy.consumption.models.dto.ResponseDto;
import com.quick.energy.consumption.models.pagination.PageOutputDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

/**
 * @author efe ariaroo
 */
public class RestUtil {

    /**
     * Performs validation.
     *
     * @param fields the binding result holding the fields
     * @throws ServiceException if one or more field fails the validation test
     */
    static void validate(BindingResult fields) {
        if (fields.hasErrors()) {
            final FieldError fieldError = fields.getFieldError();
            if (fieldError != null) {
                throw new ServiceException("field errors: " + fieldError, fieldError.getDefaultMessage());
            }
        }
    }

    static ResponseEntity<ResponseDto> created(String whatWasCreated, Serializable data, String location) {
        return ResponseEntity.status(HttpStatus.CREATED).location(URI.create(location))
                .body(new ResponseDto(ResponseDto.Status.success, createdPhrase(whatWasCreated), data));
    }

    public static ResponseEntity<ResponseDto> response(HttpStatus httpStatus, ResponseDto.Status status, String message) {
        return response(httpStatus, status, message, null, null);
    }

    public static ResponseEntity<ResponseDto> response(HttpStatus httpStatus, ResponseDto.Status status, String message, Object data) {
        return response(httpStatus, status, message, data, null);
    }

    private static ResponseEntity<ResponseDto> response(HttpStatus httpStatus, ResponseDto.Status status, String message, Object data, String code) {
        return ResponseEntity.status(httpStatus).body(new ResponseDto(status, message, (Serializable) data, code));
    }

    public static ResponseEntity<Serializable> respondWithPlainPojo(HttpStatus httpStatus, ResponseDto.Status status, Object data) {
        return ResponseEntity.status(httpStatus).body((Serializable) data);
    }


    private static ResponseEntity<ResponseDto> response(HttpStatus httpStatus, ResponseDto.Status status, String message, Object data, String code, List<ResponseDto.ResponseError> errors) {
        return ResponseEntity.status(httpStatus).body(new ResponseDto(status, message, null, (Serializable) data, code, errors));
    }

    static ResponseEntity<ResponseDto> retrievedOne(String name, Serializable dto) {
        return response(HttpStatus.OK, ResponseDto.Status.success, RestUtil.retrievedPhrase(name), dto);
    }

    static ResponseEntity<ResponseDto> retrievedMany(String singular, String plural, List<? extends Serializable> dtos) {
        return response(HttpStatus.OK, ResponseDto.Status.success, describeList(dtos, singular, plural), dtos);
    }

    static ResponseEntity<ResponseDto> retrievedMany(String singular, String plural, PageOutputDto pageOutput) {
        return response(HttpStatus.OK, ResponseDto.Status.success, describeList(pageOutput.getElements(), singular, plural), pageOutput);
    }

    private static String createdPhrase(String what) {
        return String.format("Okay, %s was successfully created.", what);
    }

    private static String retrievedPhrase(String what) {
        return String.format("Okay, %s was found.", what);
    }

    private static String describeList(List<? extends Serializable> dtos, String singular, String plural) {
        return String.format("Okay, %s %s found.",
                             dtos.isEmpty() ? "no"
                             : dtos.size() == 1 ? 1
                               : dtos.size(),
                             dtos.size() > 1 ? plural : singular);
    }
}
