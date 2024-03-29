/*
 * Copyright 2017-2024 original authors
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

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.chatbots.ChatBotType;
import io.micronaut.starter.feature.chatbots.ChatBots;
import io.micronaut.starter.feature.chatbots.telegram.template.aboutCommandHandlerGroovy;
import io.micronaut.starter.feature.chatbots.telegram.template.aboutCommandHandlerGroovyJunit;
import io.micronaut.starter.feature.chatbots.telegram.template.aboutCommandHandlerGroovySpock;
import io.micronaut.starter.feature.chatbots.telegram.template.aboutCommandHandlerJava;
import io.micronaut.starter.feature.chatbots.telegram.template.aboutCommandHandlerJavaJunit;
import io.micronaut.starter.feature.chatbots.telegram.template.aboutCommandHandlerKotlin;
import io.micronaut.starter.feature.chatbots.telegram.template.aboutCommandHandlerKotlinJunit;
import io.micronaut.starter.feature.chatbots.telegram.template.finalCommandHandlerGroovy;
import io.micronaut.starter.feature.chatbots.telegram.template.finalCommandHandlerJava;
import io.micronaut.starter.feature.chatbots.telegram.template.finalCommandHandlerKotlin;
import io.micronaut.starter.feature.chatbots.telegram.template.mockAboutCommandJson;
import io.micronaut.starter.feature.chatbots.telegram.template.telegramReadme;
import io.micronaut.starter.feature.chatbots.telegram.template.about;
import io.micronaut.starter.feature.validator.MicronautValidationFeature;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;

/**
 * Base class for Telegram chatbot features.
 *
 * @since 4.3.0
 * @author Tim Yates
 */
abstract class ChatBotsTelegram extends ChatBots {

    protected ChatBotsTelegram(MicronautValidationFeature validationFeature) {
        super(validationFeature);
    }

    @Override
    protected void addConfigurations(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put(
                "micronaut.chatbots.telegram.bots.example.token",
                "WEBHOOK_TOKEN"
        );
        generatorContext.getConfiguration().put(
                "micronaut.chatbots.telegram.bots.example.at-username",
                "@MyMicronautExampleBot"
        );
        generatorContext.getConfiguration().put(
                "micronaut.chatbots.folder",
                "botcommands"
        );
    }

    @Override
    protected void renderTemplates(GeneratorContext generatorContext) {
        generatorContext.addTemplate(
                "about-markdown",
                new RockerTemplate("src/main/resources/botcommands/about.md", about.template())
        );
        generatorContext.addTemplate(
                "about-command-handler",
                generatorContext.getSourcePath("/{packagePath}/AboutCommandHandler"),
                aboutCommandHandlerJava.template(generatorContext.getProject()),
                aboutCommandHandlerKotlin.template(generatorContext.getProject()),
                aboutCommandHandlerGroovy.template(generatorContext.getProject())
        );
        if (!generatorContext.getTestFramework().isKotlinTestFramework()) {
            generatorContext.addTemplate(
                    "mock-about-command-json",
                    new RockerTemplate(
                            "src/test/resources/mockAboutCommand.json",
                            mockAboutCommandJson.template()
                    )
            );
        }
        if (generatorContext.getTestFramework() == TestFramework.JUNIT) {
            generatorContext.addTemplate(
                    "about-command-handler-junit-test",
                    generatorContext.getTestSourcePath("/{packagePath}/AboutCommandHandler"),
                    aboutCommandHandlerJavaJunit.template(generatorContext.getProject()),
                    aboutCommandHandlerKotlinJunit.template(generatorContext.getProject()),
                    aboutCommandHandlerGroovyJunit.template(generatorContext.getProject())
            );
        } else if (generatorContext.getTestFramework() == TestFramework.SPOCK) {
            generatorContext.addTemplate(
                    "about-command-handler-spock-groovy-test",
                    new RockerTemplate(generatorContext.getTestSourcePath("/{packagePath}/AboutCommandHandler"), aboutCommandHandlerGroovySpock.template(generatorContext.getProject()))
            );
        }

        generatorContext.addHelpTemplate(new RockerWritable(telegramReadme.template(rootReadMeTemplate(generatorContext))));

        generatorContext.addTemplate(
                "final-command-handler",
                generatorContext.getSourcePath("/{packagePath}/FinalCommandHandler"),
                finalCommandHandlerJava.template(generatorContext.getProject()),
                finalCommandHandlerKotlin.template(generatorContext.getProject()),
                finalCommandHandlerGroovy.template(generatorContext.getProject())
        );
    }

    @Override
    public ChatBotType getChatBotType() {
        return ChatBotType.TELEGRAM;
    }
}
