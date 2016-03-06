package com.github.rutledgepaulv.rqe.argconverters;

import com.github.rutledgepaulv.qbuilders.nodes.AbstractNode;
import com.github.rutledgepaulv.rqe.contexts.ArgConversionContext;
import com.github.rutledgepaulv.rqe.contexts.PropertyPath;
import com.github.rutledgepaulv.rqe.exceptions.UnsupportedQueryOperatorException;

import java.util.List;
import java.util.function.BiFunction;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class OperatorSpecificConverter implements ArgConverter {

    private BiFunction<String, Class<?>, AbstractNode> subqueryPipeline;
    private BiFunction<PropertyPath, Class<?>, Class<?>> resolver;

    public OperatorSpecificConverter(BiFunction<String, Class<?>, AbstractNode> subqueryPipeline,
            BiFunction<PropertyPath, Class<?>, Class<?>> resolver) {
        this.subqueryPipeline = subqueryPipeline;
        this.resolver = resolver;
    }

    @Override
    public boolean supports(ArgConversionContext context) {
        return context.getQueryOperator().doesOperatorDetermineValueType();
    }

    @Override
    public List<?> apply(ArgConversionContext context) {
        switch(context.getQueryOperator()) {
            case EXISTS:
                return context.getValues().stream().map(Boolean::valueOf).collect(toList());
            case SUBQUERY_ANY:
                return singletonList(parse(context));
            default:
                throw new UnsupportedQueryOperatorException("This converter cannot handle the operator " + context.getQueryOperator());
        }
    }

    private AbstractNode parse(ArgConversionContext context) {
        return subqueryPipeline.apply(context.getValues().iterator().next(),
                subType(context.getPropertyPath(), context.getEntityType()));
    }

    private Class<?> subType(PropertyPath propertyPath, Class<?> entityType) {
        return resolver.apply(propertyPath, entityType);
    }

}
