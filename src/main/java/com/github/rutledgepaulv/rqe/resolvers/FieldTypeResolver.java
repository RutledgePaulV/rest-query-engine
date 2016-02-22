package com.github.rutledgepaulv.rqe.resolvers;

import com.github.rutledgepaulv.rqe.contexts.PropertyPath;

import java.util.function.BiFunction;

public interface FieldTypeResolver extends BiFunction<PropertyPath, Class<?>, Class<?>> {}
