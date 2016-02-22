package com.github.rutledgepaulv.rqe.conversions;

import java.util.function.BiFunction;

public interface StringToTypeConverter extends BiFunction<String, Class<?>, Object> {

    boolean supports(Class<?> clazz);

}
