package io.micronaut.starter.build.maven;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MavenCombineAttribute {

    APPEND("combine.children=\"append\""),
    OVERRIDE("combine.self=\"override\"");

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
