package com.github.rutledgepaulv.rqe.argconverters;

import com.github.rutledgepaulv.rqe.contexts.ArgConversionContext;
import com.github.rutledgepaulv.rqe.exceptions.FailedArgumentConversionException;
import com.github.rutledgepaulv.rqe.utils.StreamUtil;

import java.util.*;

public class ConverterChain implements Iterable<ArgConverter>, ArgConverter {

    private Map<Class<? extends ArgConverter>, Boolean> switchBoard = new HashMap<>();
    private List<ArgConverter> converters = new LinkedList<>();

    public ConverterChain() {}

    public ConverterChain(Iterable<ArgConverter> converters) {
        converters.forEach(this::appendInternal);
    }

    public ConverterChain(ConverterChain clone) {
        clone.converters.forEach(this::appendInternal);
        clone.switchBoard.entrySet().stream().forEach(entry -> switchBoard.put(entry.getKey(), entry.getValue()));
    }

    public ConverterChain disable(Class<? extends ArgConverter> clazz) {
        ConverterChain result = new ConverterChain(this);
        result.switchBoard.put(clazz, false);
        return result;
    }

    public ConverterChain enable(Class<? extends ArgConverter> clazz) {
        ConverterChain result = new ConverterChain(this);
        result.switchBoard.put(clazz, true);
        return result;
    }

    public ConverterChain append(ArgConverter converter) {
        ConverterChain result = new ConverterChain(this);

        if(!result.switchBoard.containsKey(converter.getClass())) {
            result.switchBoard.put(converter.getClass(), true);
        }

        result.converters.add(converter);

        return result;
    }

    public ConverterChain prepend(ArgConverter converter) {
        ConverterChain result = new ConverterChain(this);

        if(!result.switchBoard.containsKey(converter.getClass())) {
            result.switchBoard.put(converter.getClass(), true);
        }

        result.converters.add(0, converter);

        return result;
    }

    private void appendInternal(ArgConverter converter) {
        if(!switchBoard.containsKey(converter.getClass())) {
            switchBoard.put(converter.getClass(), true);
        }

        converters.add(converter);
    }

    public Iterator<ArgConverter> iterator() {
        return converters.stream()
                .filter(converter -> switchBoard.getOrDefault(converter.getClass(), false))
                .iterator();
    }

    @Override
    public boolean supports(ArgConversionContext context) {
        return StreamUtil.fromIterator(iterator()).anyMatch(converter -> converter.supports(context));
    }

    @Override
    public List<?> apply(ArgConversionContext context) {
        return StreamUtil.fromIterator(iterator()).filter(converter ->
                converter.supports(context)).findFirst()
                .map(converter -> converter.apply(context))
                .orElseThrow(FailedArgumentConversionException::new);
    }
}
