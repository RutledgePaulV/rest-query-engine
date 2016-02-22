package com.github.rutledgepaulv.rqe.argconverters;

import com.github.rutledgepaulv.qbuilders.nodes.AbstractNode;
import com.github.rutledgepaulv.rqe.contexts.ArgConversionContext;
import com.github.rutledgepaulv.rqe.exceptions.UnsupportedQueryOperatorException;

import java.util.List;
import java.util.function.Function;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class OperatorSpecificConverter implements ArgConverter {

    private Function<String, AbstractNode> subqueryPipeline;

    public OperatorSpecificConverter(Function<String, AbstractNode> subqueryPipeline) {
        this.subqueryPipeline = subqueryPipeline;
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
                return singletonList(parse(context.getValues().iterator().next()));
            default:
                throw new UnsupportedQueryOperatorException("This converter cannot handle the operator " + context.getQueryOperator());
        }
    }

    private AbstractNode parse(String value) {
        return subqueryPipeline.apply(value);
    }

}
