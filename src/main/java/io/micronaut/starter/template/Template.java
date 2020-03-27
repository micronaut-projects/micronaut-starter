package io.micronaut.starter.template;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Template {

    String getPath();

    void write(OutputStream outputStream) throws IOException;
}
