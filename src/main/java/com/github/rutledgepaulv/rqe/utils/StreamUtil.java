/*
 *  com.github.rutledgepaulv.rqe.utils.StreamUtil
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package com.github.rutledgepaulv.rqe.utils;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtil {

    public static <T> Stream<T> fromIterator(Iterator<T> iter) {
        Iterable<T> iterable = () -> iter;
        return StreamSupport.stream(iterable.spliterator(), false);
    }


}
