package com.github.rutledgepaulv.rqe.exceptions;

public class FailedArgumentConversionException extends RuntimeException {
    public FailedArgumentConversionException() {
    }

    public FailedArgumentConversionException(String message) {
        super(message);
    }

    public FailedArgumentConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedArgumentConversionException(Throwable cause) {
        super(cause);
    }
}
