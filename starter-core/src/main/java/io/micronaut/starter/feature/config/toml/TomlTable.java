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

import java.util.Collection;
import java.util.Map;

public record TomlTable(
        TomlPath path,
        Map<String, Object> values
) {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[" + path.toString() + "]\n");
        values.forEach((key, value) -> {
            builder.append(key).append(" = ");
            appendValue(builder, value);
            builder.append('\n');
        });
        return builder.toString();
    }

    private static void appendValue(StringBuilder builder, Object value) {
        if (value instanceof String string) {
            appendQuoted(builder, string);
        } else if (value instanceof Boolean || value instanceof Number) {
            builder.append(value);
        } else if (value instanceof Collection<?> collection) {
            builder.append("[\n");
            for (Object element : collection) {
                builder.append("    ");
                appendValue(builder, element);
                builder.append(",\n");
            }
            builder.append(']');
        } else if (value instanceof Map<?, ?> map) {
            builder.append('{');
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) {
                    builder.append(", ");
                }
                appendQuoted(builder, String.valueOf(entry.getKey()));
                builder.append(" = ");
                appendValue(builder, entry.getValue());
                first = false;
            }
            builder.append('}');
        } else {
            appendQuoted(builder, String.valueOf(value));
        }
    }

    private static void appendQuoted(StringBuilder builder, String value) {
        builder.append('"').append(value.replace("\\", "\\\\").replace("\"", "\\\"")).append('"');
    }

}
