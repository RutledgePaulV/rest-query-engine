package com.github.rutledgepaulv.rqe.pipes;

import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.ElasticsearchVisitor;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.qbuilders.visitors.PredicateVisitor;
import com.github.rutledgepaulv.rqe.User;
import com.github.rutledgepaulv.rqe.adapters.GeneralQueryBuilder;
import org.junit.Test;

import java.util.function.Predicate;

public class QueryConversionPipelineTest {


    private QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();


    @Test
    public void mongo() {

        Condition<GeneralQueryBuilder> condition = pipeline.apply("firstName==Paul;age==30", User.class);
        System.out.println(condition.query(new MongoVisitor()).getCriteriaObject().toString());

    }


    @Test
    public void elasticsearch() {

        Condition<GeneralQueryBuilder> condition = pipeline.apply("firstName==Paul;age==30", User.class);
        System.out.println(condition.query(new ElasticsearchVisitor()).toString());

    }


    @Test
    public void predicate() {

        Condition<GeneralQueryBuilder> condition = pipeline.apply("firstName==Paul;age==30", User.class);
        Predicate<User> predicate = condition.query(new PredicateVisitor<>());

    }



}
