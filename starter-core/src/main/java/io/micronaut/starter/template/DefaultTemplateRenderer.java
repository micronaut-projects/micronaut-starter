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

import io.micronaut.starter.io.OutputHandler;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultTemplateRenderer implements TemplateRenderer {

    protected static final Pattern VARIABLES_PATTERN = Pattern.compile("\\{(.+?)}");

    private final Map<String, String> replacements;
    private final OutputHandler outputHandler;

    public DefaultTemplateRenderer(Map<String, String> pathReplacements,
                                   OutputHandler outputHandler) {
        this.replacements = pathReplacements;
        this.outputHandler = outputHandler;
    }

    public RenderResult render(Template template, boolean force) {
        String path = replaceVariables(template.getPath(), replacements);
        if (outputHandler.exists(path) && !force) {
            return RenderResult.skipped(path);
        }
        try {
            outputHandler.write(path, template);
            return RenderResult.success(path);
        } catch (IOException e) {
            return RenderResult.error(path, e);
        }
    }

    protected String replaceVariables(String path, Map<String, String> replacements) {
        Matcher matcher = VARIABLES_PATTERN.matcher(path);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            String replacement = replacements.get(matcher.group(1));
            builder.append(path, i, matcher.start());
            if (replacement == null) {
                builder.append(matcher.group(0));
            } else {
                builder.append(replacement);
            }
            i = matcher.end();
        }
        builder.append(path.substring(i));
        return builder.toString();
    }

    @Override
    public void close() throws IOException {
        outputHandler.close();
    }
}
