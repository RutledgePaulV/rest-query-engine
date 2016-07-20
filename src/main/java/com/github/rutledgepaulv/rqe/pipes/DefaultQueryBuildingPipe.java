/*
 *  com.github.rutledgepaulv.rqe.pipes.DefaultQueryBuildingPipe
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
import com.github.rutledgepaulv.qbuilders.nodes.AbstractNode;
import com.github.rutledgepaulv.rqe.adapters.TreeToConditionAdapter;

import java.util.function.Function;

public class DefaultQueryBuildingPipe implements Function<AbstractNode, Condition<GeneralQueryBuilder>> {

    @Override
    public Condition<GeneralQueryBuilder> apply(AbstractNode abstractNode) {
        return new TreeToConditionAdapter().apply(abstractNode);
    }

}
