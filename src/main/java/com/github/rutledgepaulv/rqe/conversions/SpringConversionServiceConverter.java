package com.github.rutledgepaulv.rqe.conversions;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

public class SpringConversionServiceConverter implements StringToTypeConverter {

    private ConversionService conversionService;

    public SpringConversionServiceConverter() {
        this.conversionService = new DefaultConversionService();
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
