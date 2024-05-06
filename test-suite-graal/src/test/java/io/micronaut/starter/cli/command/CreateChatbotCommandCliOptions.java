package io.micronaut.starter.cli.command;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.architecture.Arm;
import io.micronaut.starter.feature.architecture.CpuArchitecture;
import io.micronaut.starter.feature.aws.LambdaFunctionUrl;
import io.micronaut.starter.feature.chatbots.basecamp.BasecampAwsChatBot;
import io.micronaut.starter.feature.chatbots.basecamp.BasecampAzureChatBot;
import io.micronaut.starter.feature.chatbots.basecamp.BasecampGcpChatBot;
import io.micronaut.starter.feature.chatbots.basecamp.BasecampHttpChatBot;
import io.micronaut.starter.feature.chatbots.telegram.TelegramAwsChatBot;
import io.micronaut.starter.feature.chatbots.telegram.TelegramAzureChatBot;
import io.micronaut.starter.feature.chatbots.telegram.TelegramGcpChatBot;
import io.micronaut.starter.feature.chatbots.telegram.TelegramHttpChatBot;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import java.util.ArrayList;
import java.util.List;

class CreateChatbotCommandCliOptions implements CommandSupplier {

    final CreateChatBotBuilderCommand.ChatBotType chatBotType;
    final CreateChatBotBuilderCommand.ChatBotDeployment applicationType;
    final CpuArchitecture cpuArchitecture;
    final boolean cdk;
    final Language language;
    final TestFramework testFramework;
    final BuildTool buildTool;
    final JdkVersion javaVersion;
    int index = 0;

    CreateChatbotCommandCliOptions(
            CreateChatBotBuilderCommand.ChatBotType chatBotType,
            CreateChatBotBuilderCommand.ChatBotDeployment applicationType,
            CpuArchitecture cpuArchitecture,
            boolean cdk,
            Language language,
            TestFramework testFramework,
            BuildTool buildTool,
            JdkVersion javaVersion
    ) {
        this.chatBotType = chatBotType;
        this.applicationType = applicationType;
        this.cpuArchitecture = cpuArchitecture;
        this.cdk = cdk;
        this.language = language;
        this.testFramework = testFramework;
        this.buildTool = buildTool;
        this.javaVersion = javaVersion;
    }

    List<String> getFeatures() {
        return switch (applicationType) {
            case LAMBDA -> {
                String type = chatBotType == CreateChatBotBuilderCommand.ChatBotType.BASECAMP ? BasecampAwsChatBot.NAME : TelegramAwsChatBot.NAME;
                yield cdk ? List.of(type, cpuArchitecture.getName(), LambdaFunctionUrl.NAME) : List.of(type, cpuArchitecture.getName());
            }
            case AZURE ->
                    List.of(chatBotType == CreateChatBotBuilderCommand.ChatBotType.BASECAMP ? BasecampAzureChatBot.NAME : TelegramAzureChatBot.NAME);
            case GCP ->
                    List.of(chatBotType == CreateChatBotBuilderCommand.ChatBotType.BASECAMP ? BasecampGcpChatBot.NAME : TelegramGcpChatBot.NAME);
            case HTTP ->
                    List.of(chatBotType == CreateChatBotBuilderCommand.ChatBotType.BASECAMP ? BasecampHttpChatBot.NAME : TelegramHttpChatBot.NAME);
        };
    }

    ApplicationType getExpectedApplicationType() {
        if (applicationType == CreateChatBotBuilderCommand.ChatBotDeployment.HTTP) {
            return ApplicationType.DEFAULT;
        }
        return ApplicationType.FUNCTION;
    }

    List<String> getCliCommands() {
        List<String> ret = new ArrayList<>();
        ret.add("%d".formatted(chatBotType.ordinal() + 1));
        ret.add("%d".formatted(applicationType.ordinal() + 1));
        if (applicationType == CreateChatBotBuilderCommand.ChatBotDeployment.LAMBDA) {
            ret.add(cpuArchitecture instanceof Arm ? "1" : "2");
            ret.add(cdk ? "1" : "2");
        }
        ret.add("%d".formatted(language.ordinal() + 1));
        ret.add("%d".formatted(testFramework.ordinal() + 1));
        ret.add("%d".formatted(buildTool.ordinal() + 1));
        ret.add(javaVersion == JdkVersion.JDK_17 ? "1" : "2");
        return ret;
    }

    @Override
    public String toString() {
        if (applicationType == CreateChatBotBuilderCommand.ChatBotDeployment.LAMBDA) {
            return "%s %s %s %s %s %s %s %s".formatted(
                    chatBotType.name(),
                    applicationType.name(),
                    cpuArchitecture.getName(),
                    cdk,
                    language.name(),
                    testFramework.name(),
                    buildTool.name(),
                    javaVersion.name()
            );
        } else {
            return "%s %s %s %s %s %s".formatted(
                    chatBotType.name(),
                    applicationType.name(),
                    language.name(),
                    testFramework.name(),
                    buildTool.name(),
                    javaVersion.name()
            );
        }
    }

    public String nextCommand() {
        return getCliCommands().get(index++);
    }
}
