package com.github.rutledgepaulv.rqe.pipes;

import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.nodes.AbstractNode;
import com.github.rutledgepaulv.rqe.adapters.GeneralQueryBuilder;
import com.github.rutledgepaulv.rqe.conversions.SpringConversionServiceConverter;
import com.github.rutledgepaulv.rqe.resolvers.MongoPersistentEntityFieldTypeResolver;
import cz.jirutka.rsql.parser.ast.Node;

import java.util.function.BiFunction;
import java.util.function.Function;

public class QueryConversionPipeline implements BiFunction<String, Class<?>, Condition<GeneralQueryBuilder>> {

    public static class QueryConversionPipelineBuilder {
        private QueryConversionPipelineBuilder(){}

        private Function<String, Node> parsingPipe = new DefaultParsingPipe();
        private Function<Node, Node> preConversionTransformer = new IdentityPipe<>();
        private Function<AbstractNode, AbstractNode> postConversionTransformer = new IdentityPipe<>();
        private Function<AbstractNode, Condition<GeneralQueryBuilder>> queryBuildingPipe = new DefaultQueryBuildingPipe();
        private BiFunction<Node, Class<?>, AbstractNode> argumentConversionPipe;


        public QueryConversionPipelineBuilder useNonDefaultParsingPipe(Function<String, Node> parsingPipe) {
            this.parsingPipe = parsingPipe;
            return this;
        }

        public QueryConversionPipelineBuilder useNonDefaultPreConversionTransformer(
                Function<Node, Node> preConversionTransformer) {
            this.preConversionTransformer = preConversionTransformer;
            return this;
        }

        public QueryConversionPipelineBuilder setArgumentConversionPipe(
                BiFunction<Node, Class<?>, AbstractNode> argumentConversionPipe) {
            this.argumentConversionPipe = argumentConversionPipe;
            return this;
        }

        public QueryConversionPipelineBuilder useNonDefaultPostConversionTransformer(
                Function<AbstractNode, AbstractNode> postConversionTransformer) {
            this.postConversionTransformer = postConversionTransformer;
            return this;
        }

        public QueryConversionPipelineBuilder useNonDefaultQueryBuildingPipe(
                Function<AbstractNode, Condition<GeneralQueryBuilder>> queryBuildingPipe) {
            this.queryBuildingPipe = queryBuildingPipe;
            return this;
        }

        public QueryConversionPipeline build() {
            return new QueryConversionPipeline(parsingPipe, preConversionTransformer, argumentConversionPipe, postConversionTransformer, queryBuildingPipe);
        }
    }


    public static QueryConversionPipelineBuilder builder() {
        return new QueryConversionPipelineBuilder();
    }

    public static QueryConversionPipeline defaultPipeline() {
        return QueryConversionPipeline.builder()
                .setArgumentConversionPipe(DefaultArgumentConversionPipe.builder()
                        .setStringToTypeConverter(new SpringConversionServiceConverter())
                        .setFieldResolver(new MongoPersistentEntityFieldTypeResolver())
                        .setParsingPipe(new DefaultParsingPipe())
                        .build())
                .build();
    }


    private Function<String, Node> parsingPipe;
    private Function<Node, Node> preConversionTransformer;
    private BiFunction<Node, Class<?>, AbstractNode> argumentConversionPipe;
    private Function<AbstractNode, AbstractNode> postConversionTransformer;
    private Function<AbstractNode, Condition<GeneralQueryBuilder>> queryBuildingPipe;


    private QueryConversionPipeline(
            Function<String, Node> parsingPipe,
            Function<Node, Node> preConversionTransformer,
            BiFunction<Node, Class<?>, AbstractNode> argumentConversionPipe,
            Function<AbstractNode, AbstractNode> postConversionTransformer,
            Function<AbstractNode, Condition<GeneralQueryBuilder>> queryBuildingPipe) {

        this.parsingPipe = parsingPipe;
        this.preConversionTransformer = preConversionTransformer;
        this.argumentConversionPipe = argumentConversionPipe;
        this.postConversionTransformer = postConversionTransformer;
        this.queryBuildingPipe = queryBuildingPipe;
    }

    @Override
    public Condition<GeneralQueryBuilder> apply(String rsql, Class<?> targetEntity) {
        return reducedPipeline().apply(rsql, targetEntity);
    }

    private BiFunction<String, Class<?>, Condition<GeneralQueryBuilder>> reducedPipeline() {
        return (String string, Class<?> clazz) -> parsingPipe
                .andThen(preConversionTransformer)
                .andThen(node -> argumentConversionPipe.apply(node, clazz))
                .andThen(postConversionTransformer)
                .andThen(queryBuildingPipe).apply(string);
    }


}
