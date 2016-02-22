package com.github.rutledgepaulv.rqe.adapters;

import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.nodes.AbstractNode;
import com.github.rutledgepaulv.qbuilders.nodes.AndNode;
import com.github.rutledgepaulv.qbuilders.nodes.ComparisonNode;
import com.github.rutledgepaulv.qbuilders.nodes.OrNode;
import com.github.rutledgepaulv.qbuilders.visitors.NodeVisitor;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * This adapter allows for converting between a general tree with logical and leaf nodes into a
 * query builder instance.
 */
public class TreeToConditionAdapter implements Function<AbstractNode, Condition<GeneralQueryBuilder>> {

    @Override
    public Condition<GeneralQueryBuilder> apply(AbstractNode tree) {
        return tree.visit(new QBuilderVisitor());
    }


    private class QBuilderVisitor extends NodeVisitor<Condition<GeneralQueryBuilder>> {

        @Override
        protected Condition<GeneralQueryBuilder> visit(AndNode node) {
            return new GeneralQueryBuilder().and(node.getChildren().stream().map(this::visitAny).collect(toList()));
        }

        @Override
        protected Condition<GeneralQueryBuilder> visit(OrNode node) {
            return new GeneralQueryBuilder().or(node.getChildren().stream().map(this::visitAny).collect(toList()));
        }

        @Override
        protected Condition<GeneralQueryBuilder> visit(ComparisonNode node) {
           return new GeneralQueryBuilder().passThrough(node);
        }

    }


}
