package com.github.rutledgepaulv.rqe.pipes;

import java.util.function.Function;

public class IdentityPipe<T> implements Function<T, T> {

    @Override
    public T apply(T t) {
        return t;
    }

}
