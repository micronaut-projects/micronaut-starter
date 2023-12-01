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

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.chatbots.ChatBots;
import io.micronaut.starter.feature.chatbots.template.telegramReadme;
import io.micronaut.starter.feature.validator.MicronautValidationFeature;
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
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        generatorContext.addHelpTemplate(new RockerWritable(telegramReadme.template(
                rootReadMeTemplate(generatorContext),
                generatorContext.getProject(),
                generatorContext.getFeatures(),
                getBuildCommand(generatorContext.getBuildTool()))
        ));
    }

    @Override
    public String getChatBotType() {
        return "Telegram";
    }
}
