package com.quick.energy.consumption.exceptions;

import com.quick.energy.consumption.error.ErrorCode;

/**
 * @author efe ariaroo
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Object target; // The data object at the center of the exception.
    private final String clientMessage;
    private final ErrorCode errorCode;

    public ServiceException(String clientMessage) {
        this(clientMessage, (Throwable) null);
    }

    public ServiceException(ErrorCode errorCode) {
        this(errorCode, (Throwable) null);
    }

    public ServiceException(ErrorCode errorCode, Throwable cause) {
        this(errorCode, "", cause);
    }

    public ServiceException(String clientMessage, Throwable cause) {
        this(clientMessage, clientMessage, cause);
    }

    public ServiceException(String detailedMessage, String clientMessage) {
        this(detailedMessage, clientMessage, null);
    }

    public ServiceException(String detailedMessage, String clientMessage, Throwable cause) {
        this(null, detailedMessage, clientMessage, cause);
    }

    public ServiceException(ErrorCode errorCode, String clientMessage) {
        this(errorCode, clientMessage, clientMessage);
    }

    public ServiceException(ErrorCode errorCode, String clientMessage, Throwable cause) {
        this(errorCode, clientMessage, clientMessage, cause);
    }

    public ServiceException(ErrorCode errorCode, String detailedMessage, String clientMessage) {
        this(errorCode, detailedMessage, clientMessage, null);
    }

    public ServiceException(ErrorCode errorCode, String detailedMessage, String clientMessage, Throwable throwable) {
        super(detailedMessage, throwable);
        this.clientMessage = clientMessage;
        this.errorCode = errorCode;
    }

    public String getClientMessage() {
        return clientMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public ServiceException target(Object target) {
        this.target = target;
        return this;
    }

    public Object getTarget() {
        return target;
    }
}
