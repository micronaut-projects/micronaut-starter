/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.chatbots.basecamp;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.chatbots.basecamp.templates.basecampGroovy;
import io.micronaut.starter.feature.chatbots.basecamp.templates.basecampJava;
import io.micronaut.starter.feature.chatbots.basecamp.templates.basecampKotlin;

public final class BasecampUtils {
    public static final String BASECAMP_CHATBOTS = "https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md";

    private BasecampUtils() {
    }

    public static void addHandler(GeneratorContext generatorContext) {
        String handlerFile = generatorContext.getSourcePath("/{packagePath}/HelloWorldHandler");
        generatorContext.addTemplate("helloWorldHandler", handlerFile,
                basecampJava.template(generatorContext.getProject()),
                basecampKotlin.template(generatorContext.getProject()),
                basecampGroovy.template(generatorContext.getProject()));
    }
}
