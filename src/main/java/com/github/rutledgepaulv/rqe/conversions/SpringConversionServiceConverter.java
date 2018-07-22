/*
 *  com.github.rutledgepaulv.rqe.conversions.SpringConversionServiceConverter
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package com.github.rutledgepaulv.rqe.conversions;

import com.github.rutledgepaulv.rqe.conversions.parsers.LocalDateConverter;
import com.github.rutledgepaulv.rqe.conversions.parsers.LocalDateTimeConverter;
import com.github.rutledgepaulv.rqe.conversions.parsers.StringToInstantConverter;
import com.github.rutledgepaulv.rqe.conversions.parsers.StringToObjectBestEffortConverter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

public class SpringConversionServiceConverter implements StringToTypeConverter {

    private ConversionService conversionService;

    public SpringConversionServiceConverter() {
        DefaultConversionService conversions = new DefaultConversionService();
        conversions.addConverter(new StringToInstantConverter());
        conversions.addConverter(new StringToObjectBestEffortConverter());
        conversions.addConverter(new LocalDateConverter("yyyy-MM-dd"));
        conversions.addConverter(new LocalDateTimeConverter("yyyy-MM-dd HH:mm:ss"));
        this.conversionService = conversions;
    }

    public SpringConversionServiceConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return conversionService.canConvert(String.class, clazz);
    }

    @Override
    public Object apply(String s, Class<?> aClass) {
        return conversionService.convert(s, aClass);
    }

}
