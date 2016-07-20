/*
 *  com.github.rutledgepaulv.rqe.exceptions.FailedArgumentConversionException
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

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
