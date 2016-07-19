package com.github.rutledgepaulv.rqe.conversions;

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
