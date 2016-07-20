/*
 *  com.github.rutledgepaulv.rqe.contexts.ParseTreeContext
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package com.github.rutledgepaulv.rqe.contexts;

import com.github.rutledgepaulv.qbuilders.nodes.LogicalNode;
import com.github.rutledgepaulv.qbuilders.structures.FieldPath;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ParseTreeContext {

    private LogicalNode parent;
    private FieldPath parentPath;
    private Map<String, Object> additionalInformation = new HashMap<>();

    public LogicalNode getParent() {
        return parent;
    }

    public void setParent(LogicalNode parent) {
        this.parent = parent;
    }

    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public Optional<FieldPath> getParentPath() {
        return Optional.ofNullable(parentPath);
    }

    public ParseTreeContext setParentPath(FieldPath parentPath) {
        this.parentPath = parentPath;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParseTreeContext)) {
            return false;
        }
        ParseTreeContext that = (ParseTreeContext) o;
        return Objects.equals(parent, that.parent) &&
                Objects.equals(parentPath, that.parentPath) &&
                Objects.equals(additionalInformation, that.additionalInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, parentPath, additionalInformation);
    }
}
