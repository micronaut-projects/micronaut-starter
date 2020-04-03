package io.micronaut.starter;

import io.micronaut.starter.template.Template;

import java.io.Closeable;
import java.io.IOException;

public interface OutputHandler extends Closeable {

    void write(String path, Template contents) throws IOException;

}
