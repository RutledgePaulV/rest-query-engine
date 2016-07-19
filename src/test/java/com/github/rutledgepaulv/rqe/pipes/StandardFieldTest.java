package com.github.rutledgepaulv.rqe.pipes;

import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.rqe.testsupport.User;
import org.junit.Test;

public class StandardFieldTest extends TestBase {

    static {
        MODE = RunMode.TEST;
    }

    private QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();

    @Test
    public void stringPropertyOnRootObject() {

        Condition<GeneralQueryBuilder> condition = pipeline.apply("firstName==Paul", User.class);

        assertPredicate(condition, new User().setFirstName("Paul"));

        assertNotPredicate(condition, new User().setFirstName("Joe"));

        assertMongo(condition, "{ \"firstName\" : \"Paul\"}");

        assertElasticsearch(condition, "{\n" +
                "  \"term\" : {\n" +
                "    \"firstName\" : \"Paul\"\n" +
                "  }\n" +
                "}");

    }


    @Test
    public void numberPropertyOnRootObject() {

        Condition<GeneralQueryBuilder> condition = pipeline.apply("age==23", User.class);

        assertPredicate(condition, new User().setAge(23));

        assertNotPredicate(condition, new User().setAge(24));

        assertMongo(condition, "{ \"age\" : 23}");

        assertElasticsearch(condition, "{\n" +
                "  \"term\" : {\n" +
                "    \"age\" : 23\n" +
                "  }\n" +
                "}");

    }

    @Test
    public void booleanPropertyOnRootObject() {

        Condition<GeneralQueryBuilder> condition = pipeline.apply("enabled==true", User.class);

        assertPredicate(condition, new User().setEnabled(true));

        assertNotPredicate(condition, new User().setEnabled(false));

        assertMongo(condition, "{ \"enabled\" : true}");

        assertElasticsearch(condition, "{\n" +
                "  \"term\" : {\n" +
                "    \"enabled\" : true\n" +
                "  }\n" +
                "}");

    }

}
