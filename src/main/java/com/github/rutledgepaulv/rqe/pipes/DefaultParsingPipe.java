/*
 *  com.github.rutledgepaulv.rqe.pipes.DefaultParsingPipe
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package com.github.rutledgepaulv.rqe.pipes;

import com.github.rutledgepaulv.rqe.operators.QueryOperator;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

import java.util.Arrays;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

public class DefaultParsingPipe implements Function<String, Node> {


    @Override
    public Node apply(String rsql) {
        return new RSQLParser(Arrays.stream(QueryOperator.values())
                .map(QueryOperator::parserOperator).collect(toSet()))
                .parse(rsql);
    }


}
