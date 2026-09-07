/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.starter.feature.config.toml;

import java.util.List;

public record TomlPath(List<String> segments) {

    public TomlPath {
        segments = List.copyOf(segments);
    }

    public TomlPath(String segment) {
        this(List.of(segment));
    }

    public static TomlPath of(String... segments) {
        return new TomlPath(List.of(segments));

    }

    @Override
    public String toString() {
        return String.join(".", segments);

    }
}
