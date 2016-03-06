package com.github.rutledgepaulv.rqe.pipes;

import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.rqe.adapters.GeneralQueryBuilder;
import com.github.rutledgepaulv.rqe.testsupport.State;
import com.github.rutledgepaulv.rqe.testsupport.User;
import org.junit.Ignore;
import org.junit.Test;

public class NestedObjectFieldTests extends TestBase {

    static {
        MODE = RunMode.TEST;
    }

    private QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();

    @Test
    public void stringPropertyOnNestedObject() {

        Condition<GeneralQueryBuilder> condition = pipeline.apply("address.street=='1 Michigan Ave'", User.class);

        User user = new User();

        user.getAddress().setStreet("1 Michigan Ave");
        assertPredicate(condition, user);

        user.getAddress().setStreet("Something else");
        assertNotPredicate(condition, user);

        assertMongo(condition, "{ \"address.street\" : \"1 Michigan Ave\"}");

        assertElasticsearch(condition, "{\n" +
                "  \"term\" : {\n" +
                "    \"address.street\" : \"1 Michigan Ave\"\n" +
                "  }\n" +
                "}");

    }

    @Test
    public void numberPropertyOnNestedObject() {

        Condition<GeneralQueryBuilder> condition = pipeline.apply("address.unit==100", User.class);

        User user = new User();

        user.getAddress().setUnit(100);
        assertPredicate(condition, user);

        user.getAddress().setUnit(400);
        assertNotPredicate(condition, user);

        assertMongo(condition, "{ \"address.unit\" : 100}");

        assertElasticsearch(condition, "{\n" +
                "  \"term\" : {\n" +
                "    \"address.unit\" : 100\n" +
                "  }\n" +
                "}");

    }

    @Test
    public void booleanPropertyOnNestedObject() {

        Condition<GeneralQueryBuilder> condition = pipeline.apply("address.hasCat==false", User.class);

        User user = new User();

        user.getAddress().setHasCat(false);
        assertPredicate(condition, user);

        user.getAddress().setHasCat(true);
        assertNotPredicate(condition, user);

        assertMongo(condition, "{ \"address.hasCat\" : false}");

        assertElasticsearch(condition, "{\n" +
                "  \"term\" : {\n" +
                "    \"address.hasCat\" : false\n" +
                "  }\n" +
                "}");

    }


    @Test
    public void enumPropertyOnNestedObject_mongodbAndElasticsearch() {
        Condition<GeneralQueryBuilder> condition = pipeline.apply("address.state==ILLINOIS", User.class);

        assertMongo(condition, "{ \"address.state\" : \"ILLINOIS\"}");

        assertElasticsearch(condition, "{\n" +
                "  \"term\" : {\n" +
                "    \"address.state\" : \"ILLINOIS\"\n" +
                "  }\n" +
                "}");
    }


    @Test
    @Ignore(value = "Fails to convert to enum type when reading back from json node. This is a limitation" +
            " of using jackson for our predicate implementation. Really we should be using an object graph framework. Issue#9")
    public void enumPropertyOnNestedObject_predicate() {
        Condition<GeneralQueryBuilder> condition = pipeline.apply("address.state==ILLINOIS", User.class);

        User user = new User();

        user.getAddress().setState(State.ILLINOIS);
        assertPredicate(condition, user);

        user.getAddress().setState(State.MINNESOTA);
        assertNotPredicate(condition, user);
    }

}
