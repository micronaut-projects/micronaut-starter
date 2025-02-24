package io.micronaut.starter.cli.openrewrite;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

public class ConsumerOutputStream extends OutputStream {
    private final Consumer<String> consumer;
    private final StringBuilder buffer = new StringBuilder();

    public ConsumerOutputStream(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void write(int b) throws IOException {
        if (b == '\n') { // Flush line when a newline is encountered
            flushBuffer();
        } else {
            buffer.append((char) b);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            write(b[i]);
        }
    }

    @Override
    public void flush() throws IOException {
        flushBuffer();
    }

    private void flushBuffer() {
        if (buffer.length() > 0) {
            consumer.accept(buffer.toString());
            buffer.setLength(0); // Clear buffer
        }
    }

    @Override
    public void close() throws IOException {
        flushBuffer();
        super.close();
    }
}
