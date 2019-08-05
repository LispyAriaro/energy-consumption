package com.quick.energy.consumption.exceptions;

public abstract class BaseException extends Exception {
    public BaseException() { }

    public BaseException(String message, Throwable inner) {
        super(message, inner);
    }

    public BaseException(Throwable inner) {
        super((String)null, inner);
    }

    public BaseException(String message) {
        super(message);
    }
}
