package com.github.rutledgepaulv.rqe.exceptions;

public class UnsupportedQueryOperatorException extends RuntimeException {

    public UnsupportedQueryOperatorException() {
    }

    public UnsupportedQueryOperatorException(String message) {
        super(message);
    }

    public UnsupportedQueryOperatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedQueryOperatorException(Throwable cause) {
        super(cause);
    }

}
