package com.github.rutledgepaulv.rqe.pipes;

import com.github.rutledgepaulv.qbuilders.nodes.AbstractNode;
import com.github.rutledgepaulv.rqe.argconverters.ConverterChain;
import com.github.rutledgepaulv.rqe.argconverters.EntityFieldTypeConverter;
import com.github.rutledgepaulv.rqe.argconverters.OperatorSpecificConverter;
import com.github.rutledgepaulv.rqe.contexts.ArgConversionContext;
import com.github.rutledgepaulv.rqe.contexts.ParseTreeContext;
import com.github.rutledgepaulv.rqe.contexts.PropertyPath;
import com.github.rutledgepaulv.rqe.conversions.StringToTypeConverter;
import com.github.rutledgepaulv.rqe.operators.QueryOperator;
import cz.jirutka.rsql.parser.ast.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class DefaultArgumentConversionPipe implements BiFunction<Node, Class<?>, AbstractNode> {


    public static class DefaultArgumentConversionPipeBuilder {

        private Function<String, Node> parsingPipe;
        private StringToTypeConverter stringToTypeConverter;
        private BiFunction<PropertyPath, Class<?>, Class<?>> fieldResolver;

        public DefaultArgumentConversionPipeBuilder setParsingPipe(Function<String, Node> parsingPipe) {
            this.parsingPipe = parsingPipe;
            return this;
        }

        public DefaultArgumentConversionPipeBuilder setStringToTypeConverter(
                StringToTypeConverter stringToTypeConverter) {
            this.stringToTypeConverter = stringToTypeConverter;
            return this;
        }

        public DefaultArgumentConversionPipeBuilder setFieldResolver(
                BiFunction<PropertyPath, Class<?>, Class<?>> fieldResolver) {
            this.fieldResolver = fieldResolver;
            return this;
        }

        public DefaultArgumentConversionPipe build() {
            return new DefaultArgumentConversionPipe(parsingPipe, stringToTypeConverter, fieldResolver);
        }
    }

    public static DefaultArgumentConversionPipeBuilder builder() {
        return new DefaultArgumentConversionPipeBuilder();
    }

    private Function<String, Node> parsingPipe;
    private StringToTypeConverter stringToTypeConverter;
    private BiFunction<PropertyPath, Class<?>, Class<?>> fieldResolver;


    private DefaultArgumentConversionPipe(
            Function<String, Node> parsingPipe,
            StringToTypeConverter stringToTypeConverter,
            BiFunction<PropertyPath, Class<?>, Class<?>> fieldResolver) {

        this.parsingPipe = Objects.requireNonNull(parsingPipe);
        this.stringToTypeConverter = Objects.requireNonNull(stringToTypeConverter);
        this.fieldResolver = Objects.requireNonNull(fieldResolver);
    }


    @Override
    public AbstractNode apply(Node node, Class<?> entityClass) {
        ConverterChain chain = new ConverterChain();
        chain = chain.append(new OperatorSpecificConverter(subqueryPipeline(this, entityClass)));
        chain = chain.append(new EntityFieldTypeConverter(fieldResolver, stringToTypeConverter));
        return node.accept(new ConvertingVisitor(entityClass, chain), new ParseTreeContext());
    }

    private Function<String, AbstractNode> subqueryPipeline(BiFunction<Node, Class<?>, AbstractNode> pipe, Class<?> clazz) {
        return parsingPipe.andThen(node -> pipe.apply(node, clazz));
    }


    private class ConvertingVisitor implements RSQLVisitor<AbstractNode, ParseTreeContext> {

        private ConverterChain converterChain;
        private Class<?> entityClass;

        public ConvertingVisitor(Class<?> entityClass, ConverterChain chain) {
            this.entityClass = entityClass;
            this.converterChain = chain;
        }

        @Override
        public AbstractNode visit(AndNode node, ParseTreeContext param) {
            List<AbstractNode> children = new LinkedList<>();

            com.github.rutledgepaulv.qbuilders.nodes.AndNode parent =
                    new com.github.rutledgepaulv.qbuilders.nodes.AndNode(param.getParent(), children);

            param.setParent(parent);
            children.addAll(visitChildren(node, param));

            return parent;
        }

        @Override
        public AbstractNode visit(OrNode node, ParseTreeContext param) {
            List<AbstractNode> children = new LinkedList<>();

            com.github.rutledgepaulv.qbuilders.nodes.OrNode parent =
                    new com.github.rutledgepaulv.qbuilders.nodes.OrNode(param.getParent(), children);

            param.setParent(parent);
            children.addAll(visitChildren(node, param));

            return parent;
        }

        @Override
        public AbstractNode visit(ComparisonNode node, ParseTreeContext param) {

            PropertyPath path = new PropertyPath(node.getSelector());
            ArgConversionContext context = new ArgConversionContext(path, entityClass, node.getArguments());

            com.github.rutledgepaulv.qbuilders.nodes.ComparisonNode leaf =
                    new com.github.rutledgepaulv.qbuilders.nodes.ComparisonNode(param.getParent());

            QueryOperator operator = QueryOperator.fromParserOperator(node.getOperator());

            leaf.setOperator(operator.qbuilderOperator());
            context.setQueryOperator(operator);

            leaf.setField(path.getRawPath());
            leaf.setValues(converterChain.apply(context));

            return leaf;
        }


        private Collection<AbstractNode> visitChildren(LogicalNode node, ParseTreeContext param) {
            return node.getChildren().stream().map(child -> {
               if(child instanceof AndNode) {
                   return this.visit((AndNode)child, param);
               } else if (child instanceof OrNode) {
                   return this.visit((OrNode)child, param);
               } else {
                   return this.visit((ComparisonNode)child, param);
               }
            }).collect(toList());
        }

    }



}
