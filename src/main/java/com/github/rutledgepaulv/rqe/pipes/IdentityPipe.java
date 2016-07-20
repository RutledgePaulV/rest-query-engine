/*
 *  com.github.rutledgepaulv.rqe.pipes.IdentityPipe
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package com.github.rutledgepaulv.rqe.pipes;

import java.util.function.Function;

public class IdentityPipe<T> implements Function<T, T> {

    @Override
    public T apply(T t) {
        return t;
    }

}
