package com.github.rutledgepaulv.rqe.adapters;

import com.github.rutledgepaulv.qbuilders.builders.QBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.nodes.ComparisonNode;

/**
 * A query builder instance that provides a passThrough method for constructing
 * a query builder tree out of comparison nodes rather than calling the typed
 * methods.
 *
 */
public final class GeneralQueryBuilder extends QBuilder<GeneralQueryBuilder> {

    Condition<GeneralQueryBuilder> passThrough(ComparisonNode node) {
        return condition(node.getField(), node.getOperator(), node.getValues());
    }

}
