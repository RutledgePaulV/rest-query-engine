package com.github.rutledgepaulv.rqe.pipes;

import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.nodes.AbstractNode;
import com.github.rutledgepaulv.rqe.adapters.GeneralQueryBuilder;
import com.github.rutledgepaulv.rqe.adapters.TreeToConditionAdapter;

import java.util.function.Function;

public class DefaultQueryBuildingPipe implements Function<AbstractNode, Condition<GeneralQueryBuilder>> {

    @Override
    public Condition<GeneralQueryBuilder> apply(AbstractNode abstractNode) {
        return new TreeToConditionAdapter().apply(abstractNode);
    }

}
