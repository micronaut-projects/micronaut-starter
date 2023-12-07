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
import io.micronaut.starter.feature.chatbots.template.*;
import io.micronaut.starter.feature.validator.MicronautValidationFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
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
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    protected void renderTemplates(GeneratorContext generatorContext) {
        super.renderTemplates(generatorContext);
        if (generatorContext.getTestFramework() == TestFramework.JUNIT) {
            generatorContext.addTemplate(
                    "http-client-command-handler-junit-test",
                    generatorContext.getTestSourcePath("/{packagePath}/TelegramController"),
                    controllerJavaJunit.template(generatorContext.getProject()),
                    controllerKotlinJunit.template(generatorContext.getProject()),
                    controllerGroovyJunit.template(generatorContext.getProject())
            );
        } else if (generatorContext.getTestFramework() == TestFramework.SPOCK) {
            generatorContext.addTemplate(
                    "http-client-command-handler-spock-test",
                    new RockerTemplate(generatorContext.getTestSourcePath("/{packagePath}/TelegramController"), controllerGroovySpock.template(generatorContext.getProject()))
            );
        }
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
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
        return controllerReadme.class.getName().replace(".", "/") + ".rocker.raw";
    }

    @Override
    protected String getBuildCommand(BuildTool buildTool) {
        return "";
    }
}
