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
package io.micronaut.starter.feature.chatbots.telegram;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.chatbots.template.gcpReadme;
import io.micronaut.starter.feature.validator.MicronautValidationFeature;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

/**
 * Adds support for Telegram chatbots as Google Cloud Functions.
 *
 * @author Tim Yates
 * @since 4.3.0
 */
@Singleton
public class TelegramHttpChatBot extends ChatBotsTelegram {

    public static final String NAME = "chatbots-telegram-http";

    public static final Dependency CHATBOTS_TELEGRAM_HTTP = MicronautDependencyUtils
            .chatBotsDependency()
            .artifactId("micronaut-chatbots-telegram-http")
            .compile()
            .build();

    public TelegramHttpChatBot(MicronautValidationFeature validationFeature) {
        super(validationFeature);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected String getBuildCommand(BuildTool buildTool) {
        return "";
    }

    @Override
    public String getTitle() {
        return "Telegram ChatBot as a controller";
    }

    @Override
    public String getDescription() {
        return "Generates an application that implements a Telegram chatbot controller";
    }

    @Override
    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(CHATBOTS_TELEGRAM_HTTP);
    }

    @Override
    protected String rootReadMeTemplate(GeneratorContext generatorContext) {
        return gcpReadme.class.getName().replace(".", "/") + ".rocker.raw";
    }
}
