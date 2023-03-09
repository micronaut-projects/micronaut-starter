package io.micronaut.starter.core.test.feature.chatbots

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Unroll

class BasecampSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "chatbotsBasecamp"
    }

    @Unroll("test maven #feature with #language")
    void "test basecamp features with Maven"(ApplicationType applicationType, Language language, feature) {
        when:
        generateProject(language, BuildTool.MAVEN, [feature], applicationType)
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        applicationType          | language         | feature
        ApplicationType.FUNCTION | Language.JAVA    | 'chatbots-basecamp-lambda'
        ApplicationType.FUNCTION | Language.GROOVY  | 'chatbots-basecamp-lambda'
        ApplicationType.FUNCTION | Language.KOTLIN  | 'chatbots-basecamp-lambda'
        ApplicationType.FUNCTION | Language.JAVA    | 'chatbots-basecamp-gcp-function'
        ApplicationType.FUNCTION | Language.GROOVY  | 'chatbots-basecamp-gcp-function'
        ApplicationType.FUNCTION | Language.KOTLIN  | 'chatbots-basecamp-gcp-function'
        ApplicationType.FUNCTION | Language.JAVA    | 'chatbots-basecamp-azure-function'
        ApplicationType.FUNCTION | Language.GROOVY  | 'chatbots-basecamp-azure-function'
        ApplicationType.FUNCTION | Language.KOTLIN  | 'chatbots-basecamp-azure-function'
        ApplicationType.DEFAULT  | Language.JAVA    | 'chatbots-basecamp-http'
        ApplicationType.DEFAULT  | Language.GROOVY  | 'chatbots-basecamp-http'
        ApplicationType.DEFAULT  | Language.KOTLIN  | 'chatbots-basecamp-http'
    }

    @Unroll("test gradle #feature with #language")
    void "test basecamp features with gradle"(ApplicationType applicationType, Language language, BuildTool buildTool, String feature) {
        when:
        generateProject(language, buildTool, [feature], applicationType)
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        applicationType          | language         | buildTool               | feature
        ApplicationType.FUNCTION | Language.JAVA    | BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-lambda'
        ApplicationType.FUNCTION | Language.GROOVY  | BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-lambda'
        ApplicationType.FUNCTION | Language.KOTLIN  | BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-lambda'
        ApplicationType.FUNCTION | Language.JAVA    | BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-gcp-function'
        ApplicationType.FUNCTION | Language.GROOVY  | BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-gcp-function'
        ApplicationType.FUNCTION | Language.KOTLIN  | BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-gcp-function'
        ApplicationType.FUNCTION | Language.JAVA    | BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-azure-function'
        ApplicationType.FUNCTION | Language.GROOVY  | BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-azure-function'
        ApplicationType.FUNCTION | Language.KOTLIN  | BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-azure-function'
        ApplicationType.DEFAULT  | Language.JAVA    | BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-http'
        ApplicationType.DEFAULT  | Language.GROOVY  | BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-http'
        ApplicationType.DEFAULT  | Language.KOTLIN  | BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-http'
        ApplicationType.FUNCTION | Language.JAVA    | BuildTool.GRADLE        | 'chatbots-basecamp-lambda'
        ApplicationType.FUNCTION | Language.GROOVY  | BuildTool.GRADLE        | 'chatbots-basecamp-lambda'
        ApplicationType.FUNCTION | Language.KOTLIN  | BuildTool.GRADLE        | 'chatbots-basecamp-lambda'
        ApplicationType.FUNCTION | Language.JAVA    | BuildTool.GRADLE        | 'chatbots-basecamp-gcp-function'
        ApplicationType.FUNCTION | Language.GROOVY  | BuildTool.GRADLE        | 'chatbots-basecamp-gcp-function'
        ApplicationType.FUNCTION | Language.KOTLIN  | BuildTool.GRADLE        | 'chatbots-basecamp-gcp-function'
        ApplicationType.FUNCTION | Language.JAVA    | BuildTool.GRADLE        | 'chatbots-basecamp-azure-function'
        ApplicationType.FUNCTION | Language.GROOVY  | BuildTool.GRADLE        | 'chatbots-basecamp-azure-function'
        ApplicationType.FUNCTION | Language.KOTLIN  | BuildTool.GRADLE        | 'chatbots-basecamp-azure-function'
        ApplicationType.DEFAULT  | Language.JAVA    | BuildTool.GRADLE        | 'chatbots-basecamp-http'
        ApplicationType.DEFAULT  | Language.GROOVY  | BuildTool.GRADLE        | 'chatbots-basecamp-http'
        ApplicationType.DEFAULT  | Language.KOTLIN  | BuildTool.GRADLE        | 'chatbots-basecamp-http'
    }
}
