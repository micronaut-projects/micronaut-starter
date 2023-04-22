package io.micronaut.starter.core.test.feature.agorapulse.console

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.CommunityLibraries
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf
import spock.lang.Unroll

@IgnoreIf({ CommunityLibraries.IGNORE })
class ConsoleSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "agorapulse-micronaut-console"
    }

    @Unroll
    void "test maven agorapulse-micronaut-console with #language and #testFramework"(Language language, TestFramework testFramework) {
        when:
        generateProject(language, BuildTool.MAVEN, ["agorapulse-micronaut-console"], ApplicationType.DEFAULT, testFramework)
        String output = executeMaven("compile test")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        [language, testFramework] << [
                Language.values(),
                TestFramework.values()
        ].combinations()
    }

    @Unroll
    void "test gradle agorapulse-micronaut-console with #language and #testFramework"(BuildTool buildTool, Language language, TestFramework testFramework) {
        when:
        generateProject(language, buildTool, ["agorapulse-micronaut-console"], ApplicationType.DEFAULT, testFramework)
        BuildResult result = executeGradle("test")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [buildTool, language, testFramework] << [
                [BuildTool.GRADLE_KOTLIN, BuildTool.GRADLE_KOTLIN],
                Language.values(),
                TestFramework.values()
        ].combinations()
    }
}
