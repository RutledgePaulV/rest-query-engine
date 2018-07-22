package com.github.rutledgepaulv.rqe.conversions.parsers;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LocalDateTimeConverter implements Converter<String, LocalDateTime> {
 
    private final DateTimeFormatter formatter;
 
    public LocalDateTimeConverter(String dateFormat) {
        this.formatter = DateTimeFormatter.ofPattern(dateFormat);
    }
 
    @Override
    public LocalDateTime convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
 
        return LocalDateTime.parse(source, formatter);
    }
}