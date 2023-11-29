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
import io.micronaut.starter.feature.chatbots.ChatBotsGcpFunction;
import io.micronaut.starter.feature.chatbots.template.gcpReadme;
import io.micronaut.starter.feature.chatbots.template.telegramReadme;
import io.micronaut.starter.feature.function.gcp.GoogleCloudRawFunction;
import io.micronaut.starter.feature.validator.MicronautValidationFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

/**
 * Adds support for Telegram chatbots as Google Cloud Functions.
 *
 * @since 4.3.0
 * @author Tim Yates
 */
@Singleton
public class TelegramGcpChatBot extends ChatBotsGcpFunction {

    public static final String NAME = "chatbots-telegram-gcp-function";

    public static final Dependency CHATBOTS_TELEGRAM_GCP_FUNCTION = MicronautDependencyUtils
            .chatBotsDependency()
            .artifactId("micronaut-chatbots-telegram-gcp-function")
            .compile()
            .build();

    public TelegramGcpChatBot(MicronautValidationFeature validationFeature, GoogleCloudRawFunction rawFunction) {
        super(validationFeature, rawFunction);
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
        return "Telegram ChatBot as Google Cloud Function";
    }

    @Override
    public String getDescription() {
        return "Generates an application that can be deployed as an Google Cloud Function that implements a Telegram chatbot";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        generatorContext.addHelpTemplate(new RockerWritable(telegramReadme.template(
                gcpReadme.class.getName().replace(".", "/") + ".rocker.raw",
                generatorContext.getProject(),
                generatorContext.getFeatures(),
                getBuildCommand(generatorContext.getBuildTool()))
        ));
    }

    private String getBuildCommand(BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw clean package";
        } else {
            return "gradlew shadowJar";
        }
    }

    @Override
    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(CHATBOTS_TELEGRAM_GCP_FUNCTION);
    }

    @Override
    public String getChatBotType() {
        return "Telegram";
    }
}
