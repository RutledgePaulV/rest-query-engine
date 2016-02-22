package com.github.rutledgepaulv.rqe.contexts;

import com.github.rutledgepaulv.qbuilders.nodes.AbstractNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ParseTreeContext {

    private boolean root;
    private AbstractNode parent;
    private Map<String, Object> additionalInformation = new HashMap<>();


    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public AbstractNode getParent() {
        return parent;
    }

    public void setParent(AbstractNode parent) {
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
        return root == that.root &&
                Objects.equals(parent, that.parent) &&
                Objects.equals(additionalInformation, that.additionalInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, parent, additionalInformation);
    }
}
