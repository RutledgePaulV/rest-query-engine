package com.github.rutledgepaulv.rqe.argconverters;

import com.github.rutledgepaulv.rqe.contexts.ArgConversionContext;

import java.util.List;
import java.util.function.Function;

/**
 * A converter for taking a set of string arguments as part of a query value and converting
 * them into the necessary types to be used in queries against some backend.
 */
public interface ArgConverter extends Function<ArgConversionContext, List<?>> {

    /**
     * Returns true if this converter provides the ability to convert the
     * provided arguments.
     *
     * @param context The conversion context.
     *
     * @return a boolean value indicating if this converter provides the ability to convert the arguments.
     */
    boolean supports(ArgConversionContext context);

}
