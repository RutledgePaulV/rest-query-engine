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
