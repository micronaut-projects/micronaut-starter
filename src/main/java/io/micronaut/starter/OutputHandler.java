package io.micronaut.starter;

import io.micronaut.starter.template.Template;

import java.io.IOException;

public interface OutputHandler {

    void write(String path, Template contents) throws IOException;
}
