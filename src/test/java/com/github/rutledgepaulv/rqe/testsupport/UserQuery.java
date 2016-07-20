/*
 *  com.github.rutledgepaulv.rqe.testsupport.UserQuery
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

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
