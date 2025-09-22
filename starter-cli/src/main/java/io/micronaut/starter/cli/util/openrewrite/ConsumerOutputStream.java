/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.cli.util.openrewrite;

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
