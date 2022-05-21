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
package io.micronaut.starter.io;

import io.micronaut.starter.template.Template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapOutputHandler implements OutputHandler {

    private final Map<String, String> templates;

    public MapOutputHandler() {
        templates = new LinkedHashMap<>(16);
    }

    @Override
    public boolean exists(String path) {
        return templates.containsKey(path);
    }

    @Override
    public void write(String path, Template contents) throws IOException {
        if (contents.isBinary()) {
            templates.put(path, null);
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            contents.write(out);
            templates.put(path, out.toString(StandardCharsets.UTF_8.name()));
        }
    }

    @Override
    public String getOutputLocation() {
        return null;
    }

    @Override
    public void close() throws IOException { }

    public Map<String, String> getProject() {
        return templates;
    }
}
