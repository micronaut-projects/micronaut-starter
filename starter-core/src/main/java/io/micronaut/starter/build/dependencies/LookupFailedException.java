package io.micronaut.starter.build.dependencies;

public class LookupFailedException extends RuntimeException {

    public LookupFailedException(String id) {
        super(String.format("Failed to find a match for the provided id: [%s]", id));
    }
}
