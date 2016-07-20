/*
 *  com.github.rutledgepaulv.rqe.resolvers.EntityFieldTypeResolver
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package com.github.rutledgepaulv.rqe.resolvers;

import com.github.rutledgepaulv.qbuilders.structures.FieldPath;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.function.BiFunction;

import static org.apache.commons.lang3.reflect.FieldUtils.getField;

public class EntityFieldTypeResolver implements BiFunction<FieldPath, Class<?>, Class<?>> {

    @Override
    public Class<?> apply(FieldPath path, Class<?> root) {
        String[] splitField = path.asKey().split("\\.", 2);
        if(splitField.length == 1) {
            return normalize(getField(root, splitField[0], true));
        } else {
            return apply(new FieldPath(splitField[1]), normalize(getField(root, splitField[0], true)));
        }
    }

    private static Class<?> normalize(Field field) {
        if(Collection.class.isAssignableFrom(field.getType())) {
            return getFirstTypeParameterOf(field);
        } else if(field.getType().isArray()) {
            return field.getType().getComponentType();
        } else {
            return field.getType();
        }
    }

    private static Class<?> getFirstTypeParameterOf(Field field) {
        return (Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
    }

}
