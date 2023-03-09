package io.micronaut.starter.core.test.feature.chatbots

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Unroll

class TelegramSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "chatbotsTelgram"
    }

    @Unroll("test maven #feature with #language")
    void "test telegram features with Maven"(ApplicationType applicationType, Language language, feature) {
        when:
        generateProject(language, BuildTool.MAVEN, [feature], applicationType)
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        applicationType          | language         | feature
        ApplicationType.FUNCTION | Language.JAVA    | 'chatbots-telegram-lambda'
        ApplicationType.FUNCTION | Language.GROOVY  | 'chatbots-telegram-lambda'
        ApplicationType.FUNCTION | Language.KOTLIN  | 'chatbots-telegram-lambda'
        ApplicationType.FUNCTION | Language.JAVA    | 'chatbots-telegram-gcp-function'
        ApplicationType.FUNCTION | Language.GROOVY  | 'chatbots-telegram-gcp-function'
        ApplicationType.FUNCTION | Language.KOTLIN  | 'chatbots-telegram-gcp-function'
        ApplicationType.FUNCTION | Language.JAVA    | 'chatbots-telegram-azure-function'
        ApplicationType.FUNCTION | Language.GROOVY  | 'chatbots-telegram-azure-function'
        ApplicationType.FUNCTION | Language.KOTLIN  | 'chatbots-telegram-azure-function'
        ApplicationType.DEFAULT  | Language.JAVA    | 'chatbots-telegram-http'
        ApplicationType.DEFAULT  | Language.GROOVY  | 'chatbots-telegram-http'
        ApplicationType.DEFAULT  | Language.KOTLIN  | 'chatbots-telegram-http'
    }

    @Unroll("test gradle #feature with #language")
    void "test telegram features with gradle"(ApplicationType applicationType, Language language, BuildTool buildTool, String feature) {
        when:
        generateProject(language, buildTool, [feature], applicationType)
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        applicationType          | language         | buildTool               | feature
        ApplicationType.FUNCTION | Language.JAVA    | BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-lambda'
        ApplicationType.FUNCTION | Language.GROOVY  | BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-lambda'
        ApplicationType.FUNCTION | Language.KOTLIN  | BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-lambda'
        ApplicationType.FUNCTION | Language.JAVA    | BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-gcp-function'
        ApplicationType.FUNCTION | Language.GROOVY  | BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-gcp-function'
        ApplicationType.FUNCTION | Language.KOTLIN  | BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-gcp-function'
        ApplicationType.FUNCTION | Language.JAVA    | BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-azure-function'
        ApplicationType.FUNCTION | Language.GROOVY  | BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-azure-function'
        ApplicationType.FUNCTION | Language.KOTLIN  | BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-azure-function'
        ApplicationType.DEFAULT  | Language.JAVA    | BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-http'
        ApplicationType.DEFAULT  | Language.GROOVY  | BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-http'
        ApplicationType.DEFAULT  | Language.KOTLIN  | BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-http'
        ApplicationType.FUNCTION | Language.JAVA    | BuildTool.GRADLE        | 'chatbots-telegram-lambda'
        ApplicationType.FUNCTION | Language.GROOVY  | BuildTool.GRADLE        | 'chatbots-telegram-lambda'
        ApplicationType.FUNCTION | Language.KOTLIN  | BuildTool.GRADLE        | 'chatbots-telegram-lambda'
        ApplicationType.FUNCTION | Language.JAVA    | BuildTool.GRADLE        | 'chatbots-telegram-gcp-function'
        ApplicationType.FUNCTION | Language.GROOVY  | BuildTool.GRADLE        | 'chatbots-telegram-gcp-function'
        ApplicationType.FUNCTION | Language.KOTLIN  | BuildTool.GRADLE        | 'chatbots-telegram-gcp-function'
        ApplicationType.FUNCTION | Language.JAVA    | BuildTool.GRADLE        | 'chatbots-telegram-azure-function'
        ApplicationType.FUNCTION | Language.GROOVY  | BuildTool.GRADLE        | 'chatbots-telegram-azure-function'
        ApplicationType.FUNCTION | Language.KOTLIN  | BuildTool.GRADLE        | 'chatbots-telegram-azure-function'
        ApplicationType.DEFAULT  | Language.JAVA    | BuildTool.GRADLE        | 'chatbots-telegram-http'
        ApplicationType.DEFAULT  | Language.GROOVY  | BuildTool.GRADLE        | 'chatbots-telegram-http'
        ApplicationType.DEFAULT  | Language.KOTLIN  | BuildTool.GRADLE        | 'chatbots-telegram-http'
    }
}
