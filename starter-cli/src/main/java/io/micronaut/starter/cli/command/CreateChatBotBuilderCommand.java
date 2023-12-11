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
package io.micronaut.starter.cli.command;

import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.architecture.CpuArchitecture;
import io.micronaut.starter.feature.architecture.X86;
import io.micronaut.starter.feature.aws.Cdk;
import io.micronaut.starter.feature.aws.LambdaFunctionUrl;
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
        ChatBotDeployment deployment = getDeployment(chatBotType, reader);
        applicationFeatures.add(deployment.feature);
        ApplicationType applicationType = deployment.applicationType;

        if (deployment == ChatBotDeployment.LAMBDA) {
            applicationFeatures.add(LambdaFunctionUrl.NAME);
            getArchitecture(reader).ifPresent(applicationFeatures::add);
            getCdk(reader).ifPresent(f -> applicationFeatures.add(f.getName()));
        }

        Options options = getOptions(reader);
        return new GenerateOptions(applicationType, options, applicationFeatures);
    }

    private ChatBotType getChatBotType(LineReader reader) {
        // We only have one for now...
        return ChatBotType.TELEGRAM;
    }

    protected Optional<String> getArchitecture(LineReader reader) {
        List<String> archFeatures = features
                .stream()
                .filter(CpuArchitecture.class::isInstance)
                .map(Feature::getName)
                .sorted()
                .toList();
        out("Choose your Lambda Architecture. (enter for " + X86.NAME + ")");
        return Optional.ofNullable(
                getListOption(
                        archFeatures,
                        o -> o,
                        X86.NAME,
                        reader
                )
        );
    }

    protected Optional<Feature> getCdk(LineReader reader) {
        out("Do you want to generate infrastructure as code with CDK? (enter for yes)");
        return getEnumOption(YesOrNo.class, yesOrNo -> StringUtils.capitalize(yesOrNo.name().toLowerCase()), YesOrNo.YES, reader) == YesOrNo.YES
                ? features.stream().filter(Cdk.class::isInstance).findFirst()
                : Optional.empty();
    }

    private ChatBotDeployment getDeployment(ChatBotType chatBotType, LineReader reader) {
        out("Choose your preferred deployment. (enter for default)");
        if (chatBotType == ChatBotType.TELEGRAM) {
            return getEnumOption(ChatBotDeployment.class, f -> "%s - %s".formatted(f.title, f.description), ChatBotDeployment.LAMBDA, reader);
        } else {
            throw new IllegalArgumentException("Unsupported chat bot type: " + chatBotType);
        }
    }

    private enum ChatBotType {
        TELEGRAM("Telegram Chat bot");

        private final String title;

        ChatBotType(String title) {
            this.title = title;
        }
    }

    enum ChatBotDeployment {

        LAMBDA("AWS Lambda", "Deploy to AWS Lambda", TelegramAwsChatBot.NAME, ApplicationType.FUNCTION),
        AZURE("Azure Function", "Deploy to Azure as a Function", TelegramAzureChatBot.NAME, ApplicationType.FUNCTION),
        GCP("Google Cloud Function", "Deploy to Google Cloud as a Function", TelegramGcpChatBot.NAME, ApplicationType.FUNCTION),
        HTTP("HTTP", "Deploy as an HTTP Server", TelegramHttpChatBot.NAME, ApplicationType.DEFAULT);

        private final String title;
        private final String description;
        private final String feature;
        private final ApplicationType applicationType;

        ChatBotDeployment(String title, String description, String feature, ApplicationType applicationType) {
            this.title = title;
            this.description = description;
            this.feature = feature;
            this.applicationType = applicationType;
        }
    }
}
