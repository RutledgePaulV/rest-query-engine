/*
 *  com.github.rutledgepaulv.rqe.conversions.StringToTypeConverter
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package com.github.rutledgepaulv.rqe.conversions;

import java.util.function.BiFunction;

public interface StringToTypeConverter extends BiFunction<String, Class<?>, Object> {

    boolean supports(Class<?> clazz);

}
