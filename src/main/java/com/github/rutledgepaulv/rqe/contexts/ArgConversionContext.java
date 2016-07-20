
/*
 *  com.github.rutledgepaulv.rqe.contexts.ArgConversionContext
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package com.github.rutledgepaulv.rqe.contexts;

import com.github.rutledgepaulv.qbuilders.structures.FieldPath;
import com.github.rutledgepaulv.rqe.argconverters.ConverterChain;
import com.github.rutledgepaulv.rqe.operators.QueryOperator;

import java.util.*;

public class ArgConversionContext {

    private ConverterChain chain;
    private Class<?> entityType;
    private FieldPath propertyPath;
    private List<String> values;
    private QueryOperator queryOperator;
    private Map<String, Object> additionalInformation = new HashMap<>();


    public ArgConversionContext() {}

    public ArgConversionContext(ArgConversionContext clone) {
        this.entityType = clone.entityType;
        this.propertyPath = clone.propertyPath;
        this.values = new LinkedList<>(clone.values);
        this.queryOperator = clone.queryOperator;
        this.additionalInformation = new HashMap<>(clone.additionalInformation);
        this.chain = new ConverterChain(clone.chain);
    }

    public List<String> getValues() {
        return values;
    }

    public ArgConversionContext setValues(List<String> values) {
        this.values = values;
        return this;
    }

    public Class<?> getEntityType() {
        return entityType;
    }

    public ArgConversionContext setEntityType(Class<?> entityType) {
        this.entityType = entityType;
        return this;
    }

    public FieldPath getPropertyPath() {
        return propertyPath;
    }

    public ArgConversionContext setPropertyPath(FieldPath propertyPath) {
        this.propertyPath = propertyPath;
        return this;
    }

    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public ArgConversionContext setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
        return this;
    }


    public QueryOperator getQueryOperator() {
        return queryOperator;
    }

    public ArgConversionContext setQueryOperator(QueryOperator queryOperator) {
        this.queryOperator = queryOperator;
        return this;
    }

    public ConverterChain getChain() {
        return chain;
    }

    public ArgConversionContext setChain(ConverterChain chain) {
        this.chain = chain;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArgConversionContext)) {
            return false;
        }
        ArgConversionContext that = (ArgConversionContext) o;
        return Objects.equals(chain, that.chain) &&
                Objects.equals(entityType, that.entityType) &&
                Objects.equals(propertyPath, that.propertyPath) &&
                Objects.equals(values, that.values) &&
                queryOperator == that.queryOperator &&
                Objects.equals(additionalInformation, that.additionalInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chain, entityType, propertyPath, values, queryOperator, additionalInformation);
    }
}
