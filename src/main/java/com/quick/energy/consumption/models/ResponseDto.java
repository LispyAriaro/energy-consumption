package com.quick.energy.consumption.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author efe ariaroo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Status status;
    private String message;
    @JsonProperty("error_id")
    private String errorId;
    private Serializable data;
    private String code;

    List<ResponseError> errors;

    public ResponseDto() {
    }

    public ResponseDto(Status status, String message) {
        this(status, message, null);
    }

    public ResponseDto(Status status, String message, String errorId) {
        this.status = status;
        this.message = message;
        this.errorId = errorId;
    }

    public ResponseDto(Status status, String message, Serializable data) {
        this(status, message, null, data);
    }

    public ResponseDto(Status status, String message, String errorId, Serializable data) {
        this.status = status;
        this.message = message;
        this.errorId = errorId;
        this.data = data;
    }

    public ResponseDto(Status status, String message, Serializable data, String code) {
        this(status, message, null, data, code);
    }

    public ResponseDto(Status status, String message, String errorId, Serializable data, String code, List<ResponseError> errors) {
        this.status = status;
        this.message = message;
        this.errorId = errorId;
        this.data = data;
        this.code = code;
        this.errors = errors;
    }

    public ResponseDto(Status status, String message, String errorId, Serializable data, String code) {
        this.status = status;
        this.message = message;
        this.errorId = errorId;
        this.data = data;
        this.code = code;
    }

    public Status getStatus() {
        return status;
    }

    public ResponseDto setStatus(Status status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getErrorId() {
        return errorId;
    }

    public ResponseDto setErrorId(String errorId) {
        this.errorId = errorId;
        return this;
    }

    public Serializable getData() {
        return data;
    }

    public ResponseDto setData(Serializable data) {
        this.data = data;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ResponseDto setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseDto{" + "status=" + status + ", message=" + message + ", errorId=" + errorId + ", data=" + data
               + ", code=" + code + '}';
    }

    public enum Status {
        success,
        fail,
        error;
    }

    public static class ResponseError {
        public String fieldName;
        public String fieldError;

        public ResponseError(String fieldName, String fieldError) {
            this.fieldName = fieldName;
            this.fieldError = fieldError;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldError() {
            return fieldError;
        }

        public void setFieldError(String fieldError) {
            this.fieldError = fieldError;
        }
    }
}
