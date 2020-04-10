package io.micronaut.starter.build;

public interface Comment extends Property {

    @Override
    default String getKey() {
        return null;
    }

    @Override
    default String getValue() {
        return null;
    }

    @Override
    String getComment();

    @Override
    default boolean isComment() {
        return true;
    }
}
