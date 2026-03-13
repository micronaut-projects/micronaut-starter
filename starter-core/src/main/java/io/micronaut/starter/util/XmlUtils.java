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
package io.micronaut.starter.util;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public final class XmlUtils {

    private XmlUtils() {
    }

    public static void appendList(@NonNull StringBuilder xml,
                                  @NonNull String containerTag,
                                  @NonNull String itemTag,
                                  @Nullable List<@NonNull String> values,
                                  int indentLevel,
                                  boolean includeWhenEmpty) {
        if (values == null || values.isEmpty()) {
            if (!includeWhenEmpty) {
                return;
            }
            xml.append(indent(indentLevel)).append('<').append(containerTag).append(">\n");
            xml.append(indent(indentLevel)).append("</").append(containerTag).append(">\n");
            return;
        }

        xml.append(indent(indentLevel)).append('<').append(containerTag).append(">\n");
        for (String value : values) {
            appendTag(xml, itemTag, value, indentLevel + 1);
        }
        xml.append(indent(indentLevel)).append("</").append(containerTag).append(">\n");
    }

    public static void appendTag(@NonNull StringBuilder xml,
                                 @NonNull String tagName,
                                 @NonNull String value,
                                 int indentLevel) {
        xml.append(indent(indentLevel))
                .append('<').append(tagName).append('>')
                .append(xmlEscape(value))
                .append("</").append(tagName).append(">\n");
    }

    public static @NonNull String indent(int level) {
        return "    ".repeat(level);
    }

    public static @NonNull String xmlEscape(@NonNull String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
