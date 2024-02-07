package io.micronaut.starter.cli.command;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.architecture.Arm;
import io.micronaut.starter.feature.architecture.CpuArchitecture;
import io.micronaut.starter.feature.aws.Cdk;
import io.micronaut.starter.feature.aws.LambdaFunctionUrl;
import io.micronaut.starter.feature.chatbots.basecamp.BasecampAwsChatBot;
import io.micronaut.starter.feature.chatbots.basecamp.BasecampAzureChatBot;
import io.micronaut.starter.feature.chatbots.basecamp.BasecampGcpChatBot;
import io.micronaut.starter.feature.chatbots.basecamp.BasecampHttpChatBot;
import io.micronaut.starter.feature.chatbots.telegram.TelegramAwsChatBot;
import io.micronaut.starter.feature.chatbots.telegram.TelegramAzureChatBot;
import io.micronaut.starter.feature.chatbots.telegram.TelegramGcpChatBot;
import io.micronaut.starter.feature.chatbots.telegram.TelegramHttpChatBot;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CreateLambdaCommandCliOptions implements CommandSupplier {

    final CodingStyle codingStyle;
    final Feature apiFeature;
    final LambdaDeployment lambdaDeployment;
    final CpuArchitecture cpuArchitecture;
    final boolean cdk;
    final Language language;
    final TestFramework testFramework;
    final BuildTool buildTool;
    final JdkVersion javaVersion;

    final List<Feature> allApiFeatures;
    final List<Language> allLanguages;
    final List<JdkVersion> allJdkVersions;
    private int index = 0;

    CreateLambdaCommandCliOptions(
            CodingStyle codingStyle,
            Feature apiFeatures,
            List<Feature> allApiFeatures,
            LambdaDeployment lambdaDeployment,
            CpuArchitecture cpuArchitecture,
            boolean cdk,
            Language language,
            Language[] allLanguages,
            TestFramework testFramework,
            BuildTool buildTool,
            JdkVersion javaVersion,
            JdkVersion[] allJdkVersions
    ) {
        this.codingStyle = codingStyle;
        this.apiFeature = apiFeatures;
        this.allApiFeatures = allApiFeatures;
        this.lambdaDeployment = lambdaDeployment;
        this.cpuArchitecture = cpuArchitecture;
        this.cdk = cdk;
        this.language = language;
        this.allLanguages = Arrays.asList(allLanguages);
        this.testFramework = testFramework;
        this.buildTool = buildTool;
        this.javaVersion = javaVersion;
        this.allJdkVersions = Arrays.asList(allJdkVersions);
    }

    List<String> getFeatures() {
        List<String> features = new ArrayList<>();
        features.add(AwsLambda.FEATURE_NAME_AWS_LAMBDA);
        features.add(apiFeature.getName());
        if (lambdaDeployment == LambdaDeployment.NATIVE_EXECUTABLE) {
            features.add(GraalVM.FEATURE_NAME_GRAALVM);
        }
        features.add(cpuArchitecture.getName());
        if (cdk) {
            features.add(Cdk.NAME);
        }
        return features;
    }

    ApplicationType getExpectedApplicationType() {
        if (codingStyle != CodingStyle.HANDLER) {
            return ApplicationType.DEFAULT;
        }
        return ApplicationType.FUNCTION;
    }

    List<String> getCliCommands() {
        List<String> commands = new ArrayList<>();
        commands.add("" + (codingStyle.ordinal() + 1));
        commands.add("" + (allApiFeatures.indexOf(apiFeature) + 1));
        commands.add("" + (lambdaDeployment.ordinal() + 1));
        commands.add(cpuArchitecture instanceof Arm ? "1" : "2");
        commands.add(cdk ? "1" : "2");
        commands.add("" + (allLanguages.indexOf(language) + 1));
        commands.add("" + (testFramework.ordinal() + 1));
        commands.add("" + (buildTool.ordinal() + 1));
        commands.add("" + (allJdkVersions.indexOf(javaVersion) + 1));
        return commands;
    }

    @Override
    public String toString() {
        return "%s %s %s %s cdk:%s %s %s %s %s".formatted(
                codingStyle,
                apiFeature.getName(),
                lambdaDeployment,
                cpuArchitecture.getName(),
                cdk,
                language,
                testFramework,
                buildTool,
                javaVersion
        );
    }

    public String nextCommand() {
        return getCliCommands().get(index++);
    }
}
