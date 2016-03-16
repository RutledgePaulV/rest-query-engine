package com.github.rutledgepaulv.rqe.argconverters;

import com.github.rutledgepaulv.qbuilders.nodes.AbstractNode;
import com.github.rutledgepaulv.qbuilders.structures.FieldPath;
import com.github.rutledgepaulv.rqe.contexts.ArgConversionContext;
import com.github.rutledgepaulv.rqe.contexts.ParseTreeContext;
import com.github.rutledgepaulv.rqe.exceptions.UnsupportedQueryOperatorException;
import com.github.rutledgepaulv.rqe.utils.TriFunction;

import java.util.List;
import java.util.function.BiFunction;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public class OperatorSpecificConverter implements ArgConverter {

    private TriFunction<String, Class<?>, ParseTreeContext, AbstractNode> subqueryPipeline;
    private BiFunction<FieldPath, Class<?>, Class<?>> resolver;

    public OperatorSpecificConverter(TriFunction<String, Class<?>, ParseTreeContext, AbstractNode> subqueryPipeline,
            BiFunction<FieldPath, Class<?>, Class<?>> resolver) {
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
        ParseTreeContext subqueryContext = new ParseTreeContext();
        subqueryContext.setParentPath(context.getPropertyPath());

        return subqueryPipeline.apply(context.getValues().iterator().next(),
                subType(context.getPropertyPath(), context.getEntityType()), subqueryContext);
    }

    private Class<?> subType(FieldPath propertyPath, Class<?> entityType) {
        return resolver.apply(propertyPath, entityType);
    }

}
