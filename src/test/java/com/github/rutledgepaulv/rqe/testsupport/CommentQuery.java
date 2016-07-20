/*
 *  com.github.rutledgepaulv.rqe.testsupport.CommentQuery
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package com.github.rutledgepaulv.rqe.testsupport;

import com.github.rutledgepaulv.qbuilders.builders.QBuilder;
import com.github.rutledgepaulv.qbuilders.properties.concrete.InstantProperty;
import com.github.rutledgepaulv.qbuilders.properties.concrete.StringProperty;

public class CommentQuery extends QBuilder<CommentQuery> {

    public StringProperty<CommentQuery> comment() {
        return string("comment");
    }

    public InstantProperty<CommentQuery> timestamp() {
        return instant("timestamp");
    }

}
