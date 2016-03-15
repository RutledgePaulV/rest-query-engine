package com.github.rutledgepaulv.rqe.pipes;

import com.github.rutledgepaulv.qbuilders.nodes.AbstractNode;
import com.github.rutledgepaulv.qbuilders.structures.FieldPath;
import com.github.rutledgepaulv.rqe.argconverters.ArgConverter;
import com.github.rutledgepaulv.rqe.argconverters.ConverterChain;
import com.github.rutledgepaulv.rqe.argconverters.EntityFieldTypeConverter;
import com.github.rutledgepaulv.rqe.argconverters.OperatorSpecificConverter;
import com.github.rutledgepaulv.rqe.contexts.ArgConversionContext;
import com.github.rutledgepaulv.rqe.contexts.ParseTreeContext;
import com.github.rutledgepaulv.rqe.contexts.PropertyPath;
import com.github.rutledgepaulv.rqe.conversions.SpringConversionServiceConverter;
import com.github.rutledgepaulv.rqe.conversions.StringToTypeConverter;
import com.github.rutledgepaulv.rqe.operators.QueryOperator;
import com.github.rutledgepaulv.rqe.resolvers.MongoPersistentEntityFieldTypeResolver;
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

        private Function<String, Node> parsingPipe = new DefaultParsingPipe();
        private StringToTypeConverter stringToTypeConverter = new SpringConversionServiceConverter();
        private BiFunction<PropertyPath, Class<?>, Class<?>> fieldResolver = new MongoPersistentEntityFieldTypeResolver();
        private List<ArgConverter> customConverters = new LinkedList<>();

        public DefaultArgumentConversionPipeBuilder useNonDefaultParsingPipe(Function<String, Node> parsingPipe) {
            this.parsingPipe = parsingPipe;
            return this;
        }

        public DefaultArgumentConversionPipeBuilder useNonDefaultStringToTypeConverter(
                StringToTypeConverter stringToTypeConverter) {
            this.stringToTypeConverter = stringToTypeConverter;
            return this;
        }

        public DefaultArgumentConversionPipeBuilder useNonDefaultFieldResolver(
                BiFunction<PropertyPath, Class<?>, Class<?>> fieldResolver) {
            this.fieldResolver = fieldResolver;
            return this;
        }

        public DefaultArgumentConversionPipeBuilder addCustomArgumentConverter(ArgConverter fieldResolver) {
            this.customConverters.add(fieldResolver);
            return this;
        }

        public DefaultArgumentConversionPipe build() {
            return new DefaultArgumentConversionPipe(this);
        }
    }

    public static DefaultArgumentConversionPipe defaults() {
        return new DefaultArgumentConversionPipeBuilder().build();
    }

    public static DefaultArgumentConversionPipeBuilder builder() {
        return new DefaultArgumentConversionPipeBuilder();
    }

    private Function<String, Node> parsingPipe;
    private StringToTypeConverter stringToTypeConverter;
    private BiFunction<PropertyPath, Class<?>, Class<?>> fieldResolver;
    private Collection<ArgConverter> customConverters = new LinkedList<>();


    private DefaultArgumentConversionPipe(DefaultArgumentConversionPipeBuilder builder) {
        this.parsingPipe = Objects.requireNonNull(builder.parsingPipe);
        this.stringToTypeConverter = Objects.requireNonNull(builder.stringToTypeConverter);
        this.fieldResolver = Objects.requireNonNull(builder.fieldResolver);
        this.customConverters.addAll(builder.customConverters);
    }


    @Override
    public AbstractNode apply(Node node, Class<?> entityClass) {
        ConverterChain chain = new ConverterChain();

        for(ArgConverter converter : customConverters) {
            chain = chain.append(converter);
        }

        chain = chain.append(new OperatorSpecificConverter(subqueryPipeline(this), fieldResolver));
        chain = chain.append(new EntityFieldTypeConverter(fieldResolver, stringToTypeConverter));

        return node.accept(new ConvertingVisitor(entityClass, chain), new ParseTreeContext());
    }

    private BiFunction<String, Class<?>, AbstractNode> subqueryPipeline(BiFunction<Node, Class<?>, AbstractNode> pipe) {
        return (rsql, clazz) -> parsingPipe.andThen(node -> pipe.apply(node, clazz)).apply(rsql);
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
            QueryOperator operator = QueryOperator.fromParserOperator(node.getOperator());

            ArgConversionContext context = new ArgConversionContext()
                    .setChain(converterChain)
                    .setEntityType(entityClass)
                    .setValues(node.getArguments())
                    .setPropertyPath(path)
                    .setQueryOperator(operator);

            com.github.rutledgepaulv.qbuilders.nodes.ComparisonNode leaf =
                    new com.github.rutledgepaulv.qbuilders.nodes.ComparisonNode(param.getParent());

            leaf.setField(new FieldPath(path.getRawPath()));
            leaf.setOperator(operator.qbuilderOperator());
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
