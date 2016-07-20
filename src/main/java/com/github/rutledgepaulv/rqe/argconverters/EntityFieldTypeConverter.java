/*
 *  com.github.rutledgepaulv.rqe.argconverters.EntityFieldTypeConverter
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package com.github.rutledgepaulv.rqe.argconverters;

import com.github.rutledgepaulv.qbuilders.structures.FieldPath;
import com.github.rutledgepaulv.rqe.contexts.ArgConversionContext;
import com.github.rutledgepaulv.rqe.conversions.StringToTypeConverter;

import java.util.List;
import java.util.function.BiFunction;

import static java.util.stream.Collectors.toList;

public class EntityFieldTypeConverter implements ArgConverter {

    private BiFunction<FieldPath, Class<?>, Class<?>> fieldTypeResolver;
    private StringToTypeConverter converter;

    public EntityFieldTypeConverter(BiFunction<FieldPath, Class<?>, Class<?>> fieldTypeResolver, StringToTypeConverter converter) {
        this.fieldTypeResolver = fieldTypeResolver;
        this.converter = converter;
    }

    @Override
    public boolean supports(ArgConversionContext context) {
        return isNotAnOperatorSpecificArgument(context) && pathResolvesToConvertibleType(context);
    }

    @Override
    public List<Object> apply(ArgConversionContext context) {
        Class<?> clazz = fieldTypeResolver.apply(context.getPropertyPath(), context.getEntityType());
        return context.getValues().stream().map(val -> converter.apply(val, clazz)).collect(toList());
    }

    private boolean isNotAnOperatorSpecificArgument(ArgConversionContext context) {
        return !context.getQueryOperator().doesOperatorDetermineValueType();
    }

    private boolean pathResolvesToConvertibleType(ArgConversionContext context) {
        Class<?> clazz = fieldTypeResolver.apply(context.getPropertyPath(), context.getEntityType());
        return converter.supports(clazz);
    }

}
