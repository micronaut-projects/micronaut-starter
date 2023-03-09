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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.chatbots.ChatbotsHttp;
import jakarta.inject.Singleton;

import static io.micronaut.starter.feature.chatbots.basecamp.BasecampUtils.BASECAMP_CHATBOTS;

@Singleton
public class BasecampChatbotsHttp implements ChatbotsHttp {
    private static final String NAME = "chatbots-basecamp-http";
    private static final String ARTIFACT_ID_CHATBOTS_BASECAMP_HTTP = "micronaut-chatbots-basecamp-http";
    private static final Dependency DEPENDENCY_CHATBOTS_BASECAMP_HTTP = MicronautDependencyUtils.chatbotsDependency()
            .artifactId(ARTIFACT_ID_CHATBOTS_BASECAMP_HTTP)
            .compile()
            .build();

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Chatbots Basecamp HTTP";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds a Controller to handle Basecamp Chatbots webhook";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ChatbotsHttp.super.apply(generatorContext);
        BasecampUtils.addHandler(generatorContext);
    }

    @Override
    public void addDependencies(@NonNull GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_CHATBOTS_BASECAMP_HTTP);
    }

    @Override
    public String getThirdPartyDocumentation() {
        return BASECAMP_CHATBOTS;
    }
}
