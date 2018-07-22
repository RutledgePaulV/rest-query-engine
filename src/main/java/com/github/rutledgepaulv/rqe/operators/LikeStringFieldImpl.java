package com.github.rutledgepaulv.rqe.operators;

import com.github.rutledgepaulv.qbuilders.builders.QBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.delegates.virtual.ListablePropertyDelegate;
import com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator;
import com.github.rutledgepaulv.qbuilders.structures.FieldPath;

import java.util.Collections;

/**
 * Created by sajjan on 12/22/16.
 */
public class LikeStringFieldImpl<T extends QBuilder<T>> extends ListablePropertyDelegate<T, String> implements LikeStringField<T> {

    public static final ComparisonOperator LIKE = new ComparisonOperator("LIKE");

    protected LikeStringFieldImpl(FieldPath field, T canonical) {
        super(field, canonical);
    }

    public final Condition<T> lexicallyAfter(String value) {
        return this.condition(this.getField(), ComparisonOperator.GT, Collections.singletonList(value));
    }

    public final Condition<T> lexicallyBefore(String value) {
        return this.condition(this.getField(), ComparisonOperator.LT, Collections.singletonList(value));
    }

    public final Condition<T> lexicallyNotAfter(String value) {
        return this.condition(this.getField(), ComparisonOperator.LTE, Collections.singletonList(value));
    }

    public final Condition<T> lexicallyNotBefore(String value) {
        return this.condition(this.getField(), ComparisonOperator.GTE, Collections.singletonList(value));
    }

    @Override
    public final Condition<T> like(String pattern) {
        return condition(getField(), LIKE, Collections.singletonList(pattern));
    }

    public Condition<T> pattern(String pattern) {
        return this.condition(this.getField(), ComparisonOperator.RE, Collections.singletonList(pattern));
    }
}