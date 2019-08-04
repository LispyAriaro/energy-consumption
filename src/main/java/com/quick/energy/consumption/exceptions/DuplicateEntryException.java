package com.quick.energy.consumption.exceptions;

public class DuplicateEntryException extends Exception {
    public DuplicateEntryException() { }

    public DuplicateEntryException(String message, Throwable inner) {
        super(message, inner);
    }

    public DuplicateEntryException(Throwable inner) {
        super((String)null, inner);
    }

    public DuplicateEntryException(String message) {
        super(message);
    }
}
