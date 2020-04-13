package io.micronaut.starter.build;

public interface Property {

    String getKey();

    String getValue();

    default String getComment() {
        return null;
    }

    default boolean isComment() {
        return getComment() != null;
    }

}
