package io.micronaut.starter.template;

import java.io.IOException;
import java.io.OutputStream;

public interface Template {

    String getPath();

    default boolean isBinary() {
        return false;
    }

    default boolean isExecutable() {
        return false;
    }

    void write(OutputStream outputStream) throws IOException;
}
