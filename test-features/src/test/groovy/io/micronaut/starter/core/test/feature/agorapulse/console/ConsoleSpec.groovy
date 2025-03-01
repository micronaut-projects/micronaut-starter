package io.micronaut.starter.core.test.feature.agorapulse.console

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Ignore
import spock.lang.IgnoreIf
import spock.lang.Unroll

@Ignore("agora community features do not support Micronaut Framework 4 yet")
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
