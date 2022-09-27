package io.micronaut.starter.core.test.feature.agorapulse.slack

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Unroll

class SlackSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return 'agorapulse-micronaut-slack'
    }

    @Unroll
    void "test maven agorapulse-micronaut-slack with #language, #testFramework and #applicationType"(
            Language language,
            TestFramework testFramework,
            ApplicationType applicationType
    ) {
        when:
        generateProject(language, BuildTool.MAVEN, ['agorapulse-micronaut-slack'], applicationType, testFramework)
        String output = executeMaven('compile test')

        then:
        output?.contains('BUILD SUCCESS')

        where:
        [language, testFramework, applicationType] << [
                Language.values(),
                TestFramework.values(),
                [ApplicationType.DEFAULT],
        ].combinations()
    }

    @Unroll
    void "test gradle agorapulse-micronaut-slack with #language, #testFramework and #applicationType using #buildTool"(
            BuildTool buildTool,
            Language language,
            TestFramework testFramework,
            ApplicationType applicationType
    ) {
        when:
        generateProject(language, buildTool, ['agorapulse-micronaut-slack'], applicationType, testFramework)
        BuildResult result = executeGradle('test')

        then:
        result?.output?.contains('BUILD SUCCESS')

        where:
        [buildTool, language, testFramework, applicationType] << [
                [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN],
                Language.values(),
                TestFramework.values(),
                [ApplicationType.DEFAULT],
        ].combinations()
    }
}
