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
package io.micronaut.starter.cli.command;

import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.architecture.CpuArchitecture;
import io.micronaut.starter.feature.architecture.X86;
import io.micronaut.starter.feature.aws.LambdaFunctionUrl;
import io.micronaut.starter.feature.chatbots.basecamp.BasecampAwsChatBot;
import io.micronaut.starter.feature.chatbots.basecamp.BasecampAzureChatBot;
import io.micronaut.starter.feature.chatbots.basecamp.BasecampGcpChatBot;
import io.micronaut.starter.feature.chatbots.basecamp.BasecampHttpChatBot;
import io.micronaut.starter.feature.chatbots.telegram.TelegramAwsChatBot;
import io.micronaut.starter.feature.chatbots.telegram.TelegramAzureChatBot;
import io.micronaut.starter.feature.chatbots.telegram.TelegramGcpChatBot;
import io.micronaut.starter.feature.chatbots.telegram.TelegramHttpChatBot;
import io.micronaut.starter.options.Options;
import org.jline.reader.LineReader;
import picocli.CommandLine.Command;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Command(name = CreateChatBotBuilderCommand.NAME, description = "A guided walk-through to create a chat bot application")
@Prototype
public class CreateChatBotBuilderCommand extends BuilderCommand {

    public static final String NAME = "create-chatbot";

    public CreateChatBotBuilderCommand(
            ProjectGenerator projectGenerator,
            List<Feature> features
    ) {
        super(projectGenerator, features);
    }

    @Override
    public GenerateOptions createGenerateOptions(LineReader reader) {
        ChatBotType chatBotType = getChatBotType(reader);
        Set<String> applicationFeatures = new HashSet<>();
        ChatBotDeployment deployment = getDeployment(reader);
        applicationFeatures.add(deployment.getFeature(chatBotType));
        ApplicationType applicationType = deployment.applicationType;

        if (deployment == ChatBotDeployment.LAMBDA) {
            getArchitecture(reader).ifPresent(applicationFeatures::add);
            getCdk(reader).ifPresent(applicationFeatures::add);
        }

        Options options = getOptions(reader);
        return new GenerateOptions(applicationType, options, applicationFeatures);
    }

    private ChatBotType getChatBotType(LineReader reader) {
        out("Choose your ChatBot type. (enter for " + ChatBotType.TELEGRAM.title + ")");
        return getEnumOption(ChatBotType.class, f -> "%s - %s".formatted(f.title, f.description), ChatBotType.TELEGRAM, reader);
    }

    protected Optional<String> getArchitecture(LineReader reader) {
        List<String> archFeatures = features
                .stream()
                .filter(CpuArchitecture.class::isInstance)
                .map(Feature::getName)
                .sorted()
                .toList();
        String defaultCpuArchitecture = X86.NAME;
        out("Choose your Lambda Architecture. (enter for " + defaultCpuArchitecture + ")");
        return Optional.ofNullable(getListOption(archFeatures, StringUtils::capitalize, defaultCpuArchitecture, reader));
    }

    protected Optional<String> getCdk(LineReader reader) {
        out("Do you want to generate infrastructure as code with CDK? (enter for yes)");
        return getYesOrNo(reader) == YesOrNo.YES
                ? Optional.of(LambdaFunctionUrl.NAME)
                : Optional.empty();
    }

    private ChatBotDeployment getDeployment(LineReader reader) {
        out("Choose your preferred deployment. (enter for default)");
        return getEnumOption(ChatBotDeployment.class, f -> "%s - %s".formatted(f.title, f.description), ChatBotDeployment.LAMBDA, reader);
    }

    enum ChatBotType {

        BASECAMP("Basecamp", "A chat bot for Basecamp chats"),
        TELEGRAM("Telegram", "A chat bot for the Telegram messaging platform");

        private final String title;
        private final String description;

        ChatBotType(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }

    enum ChatBotDeployment {

        LAMBDA("AWS Lambda", "Deploy to AWS Lambda", ApplicationType.FUNCTION, type -> switch (type) {
            case BASECAMP -> BasecampAwsChatBot.NAME;
            case TELEGRAM -> TelegramAwsChatBot.NAME;
        }),
        AZURE("Azure Function", "Deploy to Azure as a Function", ApplicationType.FUNCTION, type -> switch (type) {
            case BASECAMP -> BasecampAzureChatBot.NAME;
            case TELEGRAM -> TelegramAzureChatBot.NAME;
        }),
        GCP("Google Cloud Function", "Deploy to Google Cloud as a Function", ApplicationType.FUNCTION, type -> switch (type) {
            case BASECAMP -> BasecampGcpChatBot.NAME;
            case TELEGRAM -> TelegramGcpChatBot.NAME;
        }),
        HTTP("HTTP", "Deploy as an HTTP Server", ApplicationType.DEFAULT, type -> switch (type) {
            case BASECAMP -> BasecampHttpChatBot.NAME;
            case TELEGRAM -> TelegramHttpChatBot.NAME;
        });

        private final String title;
        private final String description;
        private final ApplicationType applicationType;
        private final Function<ChatBotType, String> featureName;

        ChatBotDeployment(
                String title,
                String description,
                ApplicationType applicationType,
                Function<ChatBotType, String> featureName
        ) {
            this.title = title;
            this.description = description;
            this.applicationType = applicationType;
            this.featureName = featureName;
        }

        String getFeature(ChatBotType chatBotType) {
            return featureName.apply(chatBotType);
        }
    }
}
