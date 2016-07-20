/*
 *  com.github.rutledgepaulv.rqe.exceptions.UnsupportedQueryOperatorException
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

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
