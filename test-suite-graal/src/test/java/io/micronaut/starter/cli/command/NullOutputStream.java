package io.micronaut.starter.cli.command;

import java.io.IOException;
import java.io.OutputStream;

class NullOutputStream extends OutputStream {

    @Override
    public void write(int b) throws IOException {
        // do nothing
    }
}
