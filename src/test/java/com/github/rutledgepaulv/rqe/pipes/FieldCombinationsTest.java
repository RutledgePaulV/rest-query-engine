/*
 *  com.github.rutledgepaulv.rqe.pipes.FieldCombinationsTest
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package com.github.rutledgepaulv.rqe.pipes;

import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.ElasticsearchVisitor;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.qbuilders.visitors.RSQLVisitor;
import com.github.rutledgepaulv.rqe.testsupport.CommentQuery;
import com.github.rutledgepaulv.rqe.testsupport.User;
import com.github.rutledgepaulv.rqe.testsupport.UserQuery;
import org.junit.Test;

import static java.time.Instant.EPOCH;
import static org.junit.Assert.assertEquals;

public class FieldCombinationsTest extends TestBase {

    static {
        MODE = RunMode.TEST;
    }


    private QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();

    @Test
    public void andTwoTopLevelProperties() {

        Condition<UserQuery> query = new UserQuery().age().eq(23).and().firstName().eq("Paul");

        String rsql = query.query(new RSQLVisitor());
        assertEquals("age==\"23\";firstName==\"Paul\"", rsql);

        Condition<GeneralQueryBuilder> parsed = pipeline.apply(rsql, User.class);

        assertEquals("(age==\"23\";firstName==\"Paul\")", parsed.query(new RSQLVisitor()));
        assertEquals(query.query(new MongoVisitor()), parsed.query(new MongoVisitor()));
        assertEquals(query.query(new ElasticsearchVisitor(), new ElasticsearchVisitor.Context()).toString(), parsed.query(new ElasticsearchVisitor(), new ElasticsearchVisitor.Context()).toString());

        assertMongo(parsed, "{ \"$and\" : [ { \"age\" : 23} , { \"firstName\" : \"Paul\"}]}");

        assertElasticsearch(parsed, "{\n" +
                "  \"bool\" : {\n" +
                "    \"must\" : [ {\n" +
                "      \"term\" : {\n" +
                "        \"age\" : 23\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"term\" : {\n" +
                "        \"firstName\" : \"Paul\"\n" +
                "      }\n" +
                "    } ]\n" +
                "  }\n" +
                "}");

    }

    @Test
    public void orTwoTopLevelProperties() {

        Condition<UserQuery> query = new UserQuery().age().eq(23).or().firstName().eq("Paul");

        String rsql = query.query(new RSQLVisitor());
        assertEquals("age==\"23\",firstName==\"Paul\"", rsql);

        Condition<GeneralQueryBuilder> parsed = pipeline.apply(rsql, User.class);

        assertEquals("(age==\"23\",firstName==\"Paul\")", parsed.query(new RSQLVisitor()));
        assertEquals(query.query(new MongoVisitor()), parsed.query(new MongoVisitor()));
        assertEquals(query.query(new ElasticsearchVisitor(), new ElasticsearchVisitor.Context()).toString(), parsed.query(new ElasticsearchVisitor(), new ElasticsearchVisitor.Context()).toString());

        assertMongo(parsed, "{ \"$or\" : [ { \"age\" : 23} , { \"firstName\" : \"Paul\"}]}");

        assertElasticsearch(parsed, "{\n" +
                "  \"bool\" : {\n" +
                "    \"should\" : [ {\n" +
                "      \"term\" : {\n" +
                "        \"age\" : 23\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"term\" : {\n" +
                "        \"firstName\" : \"Paul\"\n" +
                "      }\n" +
                "    } ]\n" +
                "  }\n" +
                "}");

    }

    @Test
    public void andTopLevelWithORedNestedQuery() {

        Condition<UserQuery> topLevel = new UserQuery().age().eq(23).and().firstName().eq("Paul");
        Condition<UserQuery> nested = new UserQuery().comments().any(new CommentQuery().comment().eq("Test").or().timestamp().eq(EPOCH));
        Condition<UserQuery> root = new UserQuery().or(topLevel, nested);

        String rsql = root.query(new RSQLVisitor());

        assertEquals("((age==\"23\";firstName==\"Paul\"),comments=q='comment==\"Test\",timestamp==\"1970-01-01T00:00:00Z\"')", rsql);

        Condition<GeneralQueryBuilder> parsed = pipeline.apply(rsql, User.class);

        assertEquals(rsql, parsed.query(new RSQLVisitor()));

        assertMongo(parsed, "{ \"$or\" : [ { \"$and\" : [ { \"age\" : 23} , " +
                "{ \"firstName\" : \"Paul\"}]} , { \"comments\" : { \"$elemMatch\" :" +
                " { \"$or\" : [ { \"comment\" : \"Test\"} , { \"timestamp\" : " +
                "{ \"$date\" : \"1970-01-01T00:00:00.000Z\"}}]}}}]}");

        assertElasticsearch(root, "{\n" +
                "  \"bool\" : {\n" +
                "    \"should\" : [ {\n" +
                "      \"bool\" : {\n" +
                "        \"must\" : [ {\n" +
                "          \"term\" : {\n" +
                "            \"age\" : 23\n" +
                "          }\n" +
                "        }, {\n" +
                "          \"term\" : {\n" +
                "            \"firstName\" : \"Paul\"\n" +
                "          }\n" +
                "        } ]\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"nested\" : {\n" +
                "        \"query\" : {\n" +
                "          \"bool\" : {\n" +
                "            \"should\" : [ {\n" +
                "              \"term\" : {\n" +
                "                \"comments.comment\" : \"Test\"\n" +
                "              }\n" +
                "            }, {\n" +
                "              \"term\" : {\n" +
                "                \"comments.timestamp\" : \"1970-01-01T00:00:00Z\"\n" +
                "              }\n" +
                "            } ]\n" +
                "          }\n" +
                "        },\n" +
                "        \"path\" : \"comments\"\n" +
                "      }\n" +
                "    } ]\n" +
                "  }\n" +
                "}");

    }

    @Test
    public void orTopLevelWithAndedNestedQuery() {

        Condition<UserQuery> topLevel = new UserQuery().age().eq(23).and().firstName().eq("Paul");
        Condition<UserQuery> nested = new UserQuery().comments().any(new CommentQuery().comment().eq("Test").and().timestamp().eq(EPOCH));
        Condition<UserQuery> root = new UserQuery().and(topLevel, nested);

        String rsql = root.query(new RSQLVisitor());

        assertEquals("((age==\"23\";firstName==\"Paul\");comments=q='comment==\"Test\";timestamp==\"1970-01-01T00:00:00Z\"')", rsql);

        Condition<GeneralQueryBuilder> parsed = pipeline.apply(rsql, User.class);

        assertEquals(rsql, parsed.query(new RSQLVisitor()));

        assertMongo(parsed, "{ \"$and\" : [ { \"$and\" : [ { \"age\" : 23} , " +
                "{ \"firstName\" : \"Paul\"}]} , { \"comments\" : { \"$elemMatch\" :" +
                " { \"$and\" : [ { \"comment\" : \"Test\"} , { \"timestamp\" : " +
                "{ \"$date\" : \"1970-01-01T00:00:00.000Z\"}}]}}}]}");

        assertElasticsearch(root, "{\n" +
                "  \"bool\" : {\n" +
                "    \"must\" : [ {\n" +
                "      \"bool\" : {\n" +
                "        \"must\" : [ {\n" +
                "          \"term\" : {\n" +
                "            \"age\" : 23\n" +
                "          }\n" +
                "        }, {\n" +
                "          \"term\" : {\n" +
                "            \"firstName\" : \"Paul\"\n" +
                "          }\n" +
                "        } ]\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"nested\" : {\n" +
                "        \"query\" : {\n" +
                "          \"bool\" : {\n" +
                "            \"must\" : [ {\n" +
                "              \"term\" : {\n" +
                "                \"comments.comment\" : \"Test\"\n" +
                "              }\n" +
                "            }, {\n" +
                "              \"term\" : {\n" +
                "                \"comments.timestamp\" : \"1970-01-01T00:00:00Z\"\n" +
                "              }\n" +
                "            } ]\n" +
                "          }\n" +
                "        },\n" +
                "        \"path\" : \"comments\"\n" +
                "      }\n" +
                "    } ]\n" +
                "  }\n" +
                "}");

    }

}
