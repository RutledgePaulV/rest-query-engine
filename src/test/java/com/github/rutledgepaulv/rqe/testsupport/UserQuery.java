package com.github.rutledgepaulv.rqe.testsupport;

import com.github.rutledgepaulv.qbuilders.builders.QBuilder;
import com.github.rutledgepaulv.qbuilders.properties.concrete.ConditionProperty;
import com.github.rutledgepaulv.qbuilders.properties.concrete.IntegerProperty;
import com.github.rutledgepaulv.qbuilders.properties.concrete.StringProperty;

public class UserQuery extends QBuilder<UserQuery> {

    public StringProperty<UserQuery> firstName() {
        return string("firstName");
    }

    public IntegerProperty<UserQuery> age() {
        return intNum("age");
    }

    public ConditionProperty<UserQuery, CommentQuery> comments() {
        return condition("comments");
    }

}
