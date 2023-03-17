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
package io.micronaut.starter.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class WritableUtils {

    private static final Logger LOG = LoggerFactory.getLogger(WritableUtils.class);

    private WritableUtils() {
    }

    public static String renderWritable(Writable writable, int indentationSpaces) {
        return renderWritableList(Collections.singletonList(writable), indentationSpaces);
    }

    public static String renderWritableList(List<Writable> writableList, int indentationSpaces) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (Writable w: writableList) {
            try {
                w.write(outputStream);

            } catch (IOException e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("IO Exception rendering Gradle Plugin extension");
                }
            }
        }
        String str = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        if (indentationSpaces == 0) {
            return str;
        }
        String[] lines = str.split("\n");
        List<String> indentedLines = new ArrayList<>();
        StringBuilder newLine = new StringBuilder();
        for (int i = 0; i < indentationSpaces; i++) {
            newLine.append(" ");
        }
        for (String originalLine : lines) {
            indentedLines.add(newLine + originalLine);
        }
        return String.join("\n", indentedLines) + "\n";
    }
}
