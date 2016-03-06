package com.github.rutledgepaulv.rqe.contexts;

import com.github.rutledgepaulv.qbuilders.nodes.LogicalNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ParseTreeContext {

    private LogicalNode parent;
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
                Objects.equals(additionalInformation, that.additionalInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, additionalInformation);
    }
}
