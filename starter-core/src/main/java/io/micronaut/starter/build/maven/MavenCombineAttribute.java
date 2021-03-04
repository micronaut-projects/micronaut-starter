package io.micronaut.starter.build.maven;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MavenCombineAttribute {
    CHILDREN_APPEND("combine.children=\"append\""),
    SELF_OVERRIDE("combine.self=\"override\"");

    private final String attribute;

    MavenCombineAttribute(String attribute) {
        this.attribute = attribute;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.attribute;
    }
}
