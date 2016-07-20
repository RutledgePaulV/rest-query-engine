/*
 *  com.github.rutledgepaulv.rqe.pipes.TestBase
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package com.github.rutledgepaulv.rqe.pipes;

import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.ElasticsearchVisitor;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.qbuilders.visitors.PredicateVisitor;
import com.github.rutledgepaulv.rqe.testsupport.CriteriaSerializer;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.function.Predicate;

import static org.junit.Assert.*;

public abstract class TestBase {

    public static RunMode MODE = RunMode.PRINT;

    public enum RunMode {
        TEST,
        PRINT
    }

    public void assertElasticsearch(Condition<?> condition, String expected) {
        QueryBuilder criteria = condition.query(new ElasticsearchVisitor(), new ElasticsearchVisitor.Context());
        String actual = criteria.toString();
        doOrPrint(() -> assertEquals(expected, actual), actual);
    }

    public void assertMongo(Condition<?> condition, String expected) {
        Criteria criteria = condition.query(new MongoVisitor());
        String actual = new CriteriaSerializer().apply(criteria);
        doOrPrint(() -> assertEquals(expected, actual), actual);
    }

    public void assertPredicate(Condition<?> condition, Object object) {
        Predicate<Object> pred = condition.query(new PredicateVisitor<>());
        assertTrue(pred.test(object));
    }

    public void assertNotPredicate(Condition<?> condition, Object object) {
        Predicate<Object> pred = condition.query(new PredicateVisitor<>());
        assertFalse(pred.test(object));
    }

    public void doOrPrint(Runnable doMe, String printMe) {
        if(MODE == RunMode.TEST) {
            doMe.run();
        } else {
            System.out.println(printMe);
        }
    }

}
