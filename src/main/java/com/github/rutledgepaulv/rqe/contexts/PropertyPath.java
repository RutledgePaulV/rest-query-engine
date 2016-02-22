package com.github.rutledgepaulv.rqe.contexts;

import java.util.Iterator;
import java.util.Objects;

import static java.util.Arrays.asList;

public class PropertyPath implements Iterable<String> {

    private String rawPath;

    public PropertyPath(String rawPath) {
        this.rawPath = rawPath;
    }

    public String getRawPath() {
        return rawPath;
    }

    public void setRawPath(String rawPath) {
        this.rawPath = rawPath;
    }

    public Iterator<String> iterator() {
        return asList(rawPath.split("\\.")).iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PropertyPath)) {
            return false;
        }
        PropertyPath strings = (PropertyPath) o;
        return Objects.equals(rawPath, strings.rawPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawPath);
    }

}
