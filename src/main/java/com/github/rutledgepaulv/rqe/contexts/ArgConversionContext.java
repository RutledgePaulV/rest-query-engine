package com.github.rutledgepaulv.rqe.contexts;

import com.github.rutledgepaulv.rqe.operators.QueryOperator;

import java.util.*;

public class ArgConversionContext {

    private Class<?> entityType;
    private PropertyPath propertyPath;
    private List<String> values;
    private QueryOperator queryOperator;
    private Map<String, Object> additionalInformation = new HashMap<>();


    public ArgConversionContext(PropertyPath path, Class<?> entityType, List<String> values) {
        this.entityType = entityType;
        this.propertyPath = path;
        this.values = values;
    }

    public ArgConversionContext(ArgConversionContext clone) {
        this.entityType = clone.entityType;
        this.propertyPath = clone.propertyPath;
        this.values = new LinkedList<>(clone.values);
        this.queryOperator = clone.queryOperator;
        this.additionalInformation = new HashMap<>(clone.additionalInformation);
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public Class<?> getEntityType() {
        return entityType;
    }

    public void setEntityType(Class<?> entityType) {
        this.entityType = entityType;
    }

    public PropertyPath getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(PropertyPath propertyPath) {
        this.propertyPath = propertyPath;
    }

    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }


    public QueryOperator getQueryOperator() {
        return queryOperator;
    }

    public void setQueryOperator(QueryOperator queryOperator) {
        this.queryOperator = queryOperator;
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
        return Objects.equals(entityType, that.entityType) &&
                Objects.equals(propertyPath, that.propertyPath) &&
                Objects.equals(values, that.values) &&
                Objects.equals(queryOperator, that.queryOperator) &&
                Objects.equals(additionalInformation, that.additionalInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityType, propertyPath, values, queryOperator, additionalInformation);
    }
}
