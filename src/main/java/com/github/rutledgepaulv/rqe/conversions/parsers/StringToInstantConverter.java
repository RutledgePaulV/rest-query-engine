package com.github.rutledgepaulv.rqe.conversions.parsers;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@WritingConverter
public class StringToInstantConverter implements Converter<String, Instant> {

    private static final DateTimeFormatter PARSER = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public Instant convert(String source) {
        return Instant.from(PARSER.parse(source));
    }

}
