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
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.chatbots.ChatBotsAzureFunction;
import io.micronaut.starter.feature.chatbots.template.azureReadme;
import io.micronaut.starter.feature.chatbots.template.telegramReadme;
import io.micronaut.starter.feature.function.azure.AzureRawFunction;
import io.micronaut.starter.feature.validator.MicronautValidationFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

/**
 * Adds support for Telegram chatbots as Azure Functions.
 *
 * @since 4.3.0
 * @author Tim Yates
 */
@Singleton
public class TelegramAzureChatBot extends ChatBotsAzureFunction {

    public static final String NAME = "chatbots-telegram-azure-function";

    public static final Dependency CHATBOTS_TELEGRAM_AZURE_FUNCTION = MicronautDependencyUtils
            .chatBotsDependency()
            .artifactId("micronaut-chatbots-telegram-azure-function")
            .compile()
            .build();

    public TelegramAzureChatBot(MicronautValidationFeature validationFeature, AzureRawFunction azureRawFunction) {
        super(validationFeature, azureRawFunction);
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

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Telegram ChatBot as Azure Function";
    }

    @Override
    public String getDescription() {
        return "Generates an application that can be deployed as an Azure Function that implements a Telegram chatbot";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        generatorContext.addHelpTemplate(new RockerWritable(telegramReadme.template(
                azureReadme.class.getName().replace(".", "/") + ".rocker.raw",
                generatorContext.getProject(),
                generatorContext.getFeatures(),
                getBuildCommand(generatorContext.getBuildTool()))
        ));
    }

    protected String getBuildCommand(BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw clean package";
        } else {
            return "gradlew azureFunctionsDeploy";
        }
    }

    @Override
    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(CHATBOTS_TELEGRAM_AZURE_FUNCTION);
    }

    @Override
    public String getChatBotType() {
        return "Telegram";
    }
}
