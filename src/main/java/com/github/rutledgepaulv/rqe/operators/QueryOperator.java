package com.github.rutledgepaulv.rqe.operators;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

import java.util.Arrays;

public enum QueryOperator {

    EQUAL(RSQLOperators.EQUAL, com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator.EQ),
    NOT_EQUAL(RSQLOperators.NOT_EQUAL, com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator.NE),
    LESS_THAN(RSQLOperators.LESS_THAN, com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator.LT),
    LESS_THAN_OR_EQUAL(RSQLOperators.LESS_THAN_OR_EQUAL, com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator.LTE),
    GREATER_THAN(RSQLOperators.GREATER_THAN, com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator.GT),
    GREATER_THAN_OR_EQUAL(RSQLOperators.GREATER_THAN_OR_EQUAL, com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator.GTE),
    IN(RSQLOperators.IN, com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator.IN),
    NOT_IN(RSQLOperators.NOT_IN, com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator.NIN),
    EXISTS(new ComparisonOperator("=ex="), com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator.EX),
    SUBQUERY_ANY(new ComparisonOperator("=q="), com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator.SUB_CONDITION_ANY);


    private ComparisonOperator parserOperator;
    private com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator qbuilderOperator;

    QueryOperator(ComparisonOperator parserOperator,
            com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator qbuilderOperator) {

        this.parserOperator = parserOperator;
        this.qbuilderOperator = qbuilderOperator;
    }


    public ComparisonOperator parserOperator() {
        return parserOperator;
    }

    public com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator qbuilderOperator() {
        return qbuilderOperator;
    }

    public boolean doesOperatorDetermineValueType() {
        return this == EXISTS || this == SUBQUERY_ANY;
    }

    public static QueryOperator fromParserOperator(ComparisonOperator op) {
        return Arrays.stream(values()).filter(el -> el.parserOperator.equals(op)).findFirst().orElse(null);
    }

    public static QueryOperator fromQBuilderOperator(com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator op) {
        return Arrays.stream(values()).filter(el -> el.qbuilderOperator.equals(op)).findFirst().orElse(null);
    }
}
