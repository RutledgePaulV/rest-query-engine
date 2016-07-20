/*
 *  com.github.rutledgepaulv.rqe.conversions.parsers.StringToObjectBestEffortConverter
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
import java.util.Objects;
import java.util.Optional;

public class StringToObjectBestEffortConverter implements Converter<String, Object> {

    private static final DateTimeFormatter PARSER = DateTimeFormatter.ISO_DATE_TIME;


    @Override
    public Object convert(String source) {
        return tryParseBoolean(source)
                .orElseGet(() -> tryParseDate(source)
                        .orElseGet(() -> tryParseNumber(source)
                                .orElse(source)));
    }


    private static Optional<Object> tryParseBoolean(String value) {
        String normalized = Objects.toString(value);
        if(Boolean.TRUE.toString().equals(normalized)){
            return Optional.of(true);
        } else if (Boolean.FALSE.toString().equals(normalized)) {
            return Optional.of(false);
        } else {
            return Optional.empty();
        }
    }


    private static Optional<Object> tryParseDate(String value) {
        try {
            return Optional.of(Instant.from(PARSER.parse(value)));
        }catch(Exception e) {
            return Optional.empty();
        }
    }


    private static Optional<Object> tryParseNumber(String value) {
        try {
            return Optional.of(Long.parseLong(value));
        } catch (NumberFormatException e) {
            try {
                return Optional.of(Double.parseDouble(value));
            } catch (NumberFormatException e2) {
                return Optional.empty();
            }
        }
    }


}
