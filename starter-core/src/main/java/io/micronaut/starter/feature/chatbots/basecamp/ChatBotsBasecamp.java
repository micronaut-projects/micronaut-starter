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
package io.micronaut.starter.feature.chatbots.basecamp;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.chatbots.ChatBotType;
import io.micronaut.starter.feature.chatbots.ChatBots;
import io.micronaut.starter.feature.chatbots.basecamp.template.aboutCommandHandlerGroovy;
import io.micronaut.starter.feature.chatbots.basecamp.template.aboutCommandHandlerGroovyJunit;
import io.micronaut.starter.feature.chatbots.basecamp.template.aboutCommandHandlerGroovySpock;
import io.micronaut.starter.feature.chatbots.basecamp.template.aboutCommandHandlerJava;
import io.micronaut.starter.feature.chatbots.basecamp.template.aboutCommandHandlerJavaJunit;
import io.micronaut.starter.feature.chatbots.basecamp.template.aboutCommandHandlerKotlin;
import io.micronaut.starter.feature.chatbots.basecamp.template.aboutCommandHandlerKotlinJunit;
import io.micronaut.starter.feature.chatbots.basecamp.template.finalCommandHandlerGroovy;
import io.micronaut.starter.feature.chatbots.basecamp.template.finalCommandHandlerJava;
import io.micronaut.starter.feature.chatbots.basecamp.template.finalCommandHandlerKotlin;
import io.micronaut.starter.feature.chatbots.basecamp.template.mockAboutCommandJson;
import io.micronaut.starter.feature.chatbots.basecamp.template.basecampReadme;
import io.micronaut.starter.feature.chatbots.basecamp.template.about;
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
abstract class ChatBotsBasecamp extends ChatBots {

    protected ChatBotsBasecamp(MicronautValidationFeature validationFeature) {
        super(validationFeature);
    }

    @Override
    protected void addConfigurations(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put(
                "micronaut.chatbots.folder",
                "botcommands"
        );
    }

    @Override
    protected void renderTemplates(GeneratorContext generatorContext) {
        generatorContext.addTemplate(
                "about-html",
                new RockerTemplate("src/main/resources/botcommands/about.html", about.template())
        );
        generatorContext.addTemplate(
                "basecamp-about-command-handler",
                generatorContext.getSourcePath("/{packagePath}/BasecampAboutCommandHandler"),
                aboutCommandHandlerJava.template(generatorContext.getProject()),
                aboutCommandHandlerKotlin.template(generatorContext.getProject()),
                aboutCommandHandlerGroovy.template(generatorContext.getProject())
        );
        if (!generatorContext.getTestFramework().isKotlinTestFramework()) {
            generatorContext.addTemplate(
                    "mock-basecamp-about-command-json",
                    new RockerTemplate(
                            "src/test/resources/mockBasecampAboutCommand.json",
                            mockAboutCommandJson.template()
                    )
            );
        }
        if (generatorContext.getTestFramework() == TestFramework.JUNIT) {
            generatorContext.addTemplate(
                    "about-command-handler-junit-test",
                    generatorContext.getTestSourcePath("/{packagePath}/BasecampAboutCommandHandler"),
                    aboutCommandHandlerJavaJunit.template(generatorContext.getProject()),
                    aboutCommandHandlerKotlinJunit.template(generatorContext.getProject()),
                    aboutCommandHandlerGroovyJunit.template(generatorContext.getProject())
            );
        } else if (generatorContext.getTestFramework() == TestFramework.SPOCK) {
            generatorContext.addTemplate(
                    "about-command-handler-spock-groovy-test",
                    new RockerTemplate(generatorContext.getTestSourcePath("/{packagePath}/BasecampAboutCommandHandler"), aboutCommandHandlerGroovySpock.template(generatorContext.getProject()))
            );
        }

        generatorContext.addHelpTemplate(new RockerWritable(basecampReadme.template(
                rootReadMeTemplate(generatorContext),
                generatorContext.getProject(),
                generatorContext.getFeatures(),
                getBuildCommand(generatorContext.getBuildTool()))
        ));

        generatorContext.addTemplate(
                "final-command-handler",
                generatorContext.getSourcePath("/{packagePath}/BasecampFinalCommandHandler"),
                finalCommandHandlerJava.template(generatorContext.getProject()),
                finalCommandHandlerKotlin.template(generatorContext.getProject()),
                finalCommandHandlerGroovy.template(generatorContext.getProject())
        );
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md";
    }

    @Override
    public ChatBotType getChatBotType() {
        return ChatBotType.BASECAMP;
    }
}
