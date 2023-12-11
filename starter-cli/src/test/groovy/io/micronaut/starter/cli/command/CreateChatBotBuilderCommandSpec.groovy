package io.micronaut.starter.cli.command

import io.micronaut.context.ApplicationContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.architecture.Arm
import io.micronaut.starter.feature.architecture.CpuArchitecture
import io.micronaut.starter.feature.architecture.X86
import io.micronaut.starter.feature.aws.LambdaFunctionUrl
import io.micronaut.starter.feature.chatbots.telegram.TelegramAwsChatBot
import io.micronaut.starter.feature.chatbots.telegram.TelegramAzureChatBot
import io.micronaut.starter.feature.chatbots.telegram.TelegramGcpChatBot
import io.micronaut.starter.feature.chatbots.telegram.TelegramHttpChatBot
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import org.jline.reader.LineReader
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class CreateChatBotBuilderCommandSpec extends Specification {

    @Shared
    @AutoCleanup
    ApplicationContext applicationContext

    def setupSpec() {
        applicationContext = ApplicationContext.run();
    }
    /*
Choose your preferred deployment. (enter for default)
*1) AWS Lambda - Deploy to AWS Lambda
 2) Azure Function - Deploy to Azure as a Function
 3) Google Cloud Function - Deploy to Google Cloud as a Function
 4) HTTP - Deploy as an HTTP Server
>

Choose your Lambda Architecture. (enter for x86)
 1) Arm
*2) X86
>

Do you want to generate infrastructure as code with CDK? (enter for yes)
*1) Yes
 2) No
>

Choose your preferred language. (enter for default)
*1) Java
 2) Groovy
 3) Kotlin
>

Choose your preferred test framework. (enter for default)
*1) JUnit
 2) Spock
 3) Kotest
>

Choose your preferred build tool. (enter for default)
 1) Gradle (Groovy)
*2) Gradle (Kotlin)
 3) Maven
>

Choose the target JDK. (enter for default)
*1) 17
 2) 21
     */

    void "test prompt"(CliOptions cliOptions) {
        given:
        CreateChatBotBuilderCommand command = applicationContext.getBean(CreateChatBotBuilderCommand)

        when:
        def reader = Stub(LineReader) {
            readLine(BuilderCommand.PROMPT.get()) >>> cliOptions.cliCommands
        }
        GenerateOptions options = command.createGenerateOptions(reader)

        then:
        cliOptions.expectedApplicationType == options.applicationType
        cliOptions.features ==~ options.features
        cliOptions.language == options.options.language
        cliOptions.buildTool == options.options.buildTool
        cliOptions.testFramework == options.options.testFramework
        cliOptions.javaVersion == options.options.javaVersion

        where:
        cliOptions << [
                CreateChatBotBuilderCommand.ChatBotDeployment.values(),
                [applicationContext.getBean(Arm), applicationContext.getBean(X86)],
                [true, false], // cdk
                [Language.JAVA, Language.GROOVY, Language.KOTLIN],
                [TestFramework.JUNIT, TestFramework.SPOCK, TestFramework.KOTEST],
                [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN, BuildTool.MAVEN],
                [JdkVersion.JDK_17, JdkVersion.JDK_21]
        ].combinations().collect { new CliOptions(*it) }

    }

    private static class CliOptions {
        final CreateChatBotBuilderCommand.ChatBotDeployment applicationType
        final CpuArchitecture cpuArchitecture
        final boolean cdk
        final Language language
        final TestFramework testFramework
        final BuildTool buildTool
        final JdkVersion javaVersion

        CliOptions(
                CreateChatBotBuilderCommand.ChatBotDeployment applicationType,
                CpuArchitecture cpuArchitecture,
                boolean cdk,
                Language language,
                TestFramework testFramework,
                BuildTool buildTool,
                JdkVersion javaVersion
        ) {
            this.applicationType = applicationType
            this.cpuArchitecture = cpuArchitecture
            this.cdk = cdk
            this.language = language
            this.testFramework = testFramework
            this.buildTool = buildTool
            this.javaVersion = javaVersion
        }

        List<String> getFeatures() {
            switch (applicationType) {
                case CreateChatBotBuilderCommand.ChatBotDeployment.LAMBDA:
                    return [TelegramAwsChatBot.NAME, cpuArchitecture.getName()] + (cdk ? [LambdaFunctionUrl.NAME] : [])
                case CreateChatBotBuilderCommand.ChatBotDeployment.AZURE:
                    return [TelegramAzureChatBot.NAME]
                case CreateChatBotBuilderCommand.ChatBotDeployment.GCP:
                    return [TelegramGcpChatBot.NAME]
                case CreateChatBotBuilderCommand.ChatBotDeployment.HTTP:
                    return [TelegramHttpChatBot.NAME]
            }
        }

        ApplicationType getExpectedApplicationType() {
            if (applicationType == CreateChatBotBuilderCommand.ChatBotDeployment.HTTP) {
                return ApplicationType.DEFAULT
            }
            return ApplicationType.FUNCTION
        }

        List<String> getCliCommands() {
            List<String> ret = [
                    "${applicationType.ordinal() + 1}".toString()
            ]
            if (applicationType == CreateChatBotBuilderCommand.ChatBotDeployment.LAMBDA) {
                ret += [
                        cpuArchitecture instanceof Arm ? "1" : "2",
                        cdk ? "1" : "2",
                ]
            }
            ret + [
                    "${language.ordinal() + 1}".toString(),
                    "${testFramework.ordinal() + 1}".toString(),
                    "${buildTool.ordinal() + 1}".toString(),
                    javaVersion == JdkVersion.JDK_17 ? "1" : "2"
            ]
        }

        @Override
        String toString() {
            if (applicationType == CreateChatBotBuilderCommand.ChatBotDeployment.LAMBDA) {
                "${applicationType.name()} ${cpuArchitecture.getName()} cdk:${cdk} $language $testFramework $buildTool $javaVersion"
            } else {
                "${applicationType.name()} $language $testFramework $buildTool $javaVersion"
            }
        }
    }
}
