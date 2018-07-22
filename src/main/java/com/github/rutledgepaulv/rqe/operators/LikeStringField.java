package com.github.rutledgepaulv.rqe.operators;

import com.github.rutledgepaulv.qbuilders.builders.QBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.properties.concrete.StringProperty;

/**
 * Created by sajjan on 12/22/16.
 */
public interface LikeStringField <T extends QBuilder<T>> extends StringProperty<T> {

    Condition<T> like(String pattern);

}
