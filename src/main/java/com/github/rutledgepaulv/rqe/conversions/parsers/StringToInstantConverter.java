/*
 *  com.github.rutledgepaulv.rqe.conversions.parsers.StringToInstantConverter
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package com.github.rutledgepaulv.rqe.conversions.parsers;

import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class StringToInstantConverter implements Converter<String, Instant> {

    private static final DateTimeFormatter PARSER = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public Instant convert(String source) {
        return Instant.from(PARSER.parse(source));
    }

}
