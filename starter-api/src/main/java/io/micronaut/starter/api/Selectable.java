package io.micronaut.starter.api;

public interface Selectable<T> {
    String getLabel();
    String getDescription();
    T getValue();

}
