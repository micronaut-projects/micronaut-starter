package io.micronaut.starter.core.test.feature.agorapulse.gru

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Ignore
import spock.lang.IgnoreIf
import spock.lang.Unroll

@Ignore("agora community features do not support Micronaut Framework 4 yet")
class GruHttpSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "agorapulse-gru-http"
    }

    @Unroll
    void "test maven agorapulse-gru-http with #language and #testFramework"(Language language, TestFramework testFramework) {
        when:
        generateProject(language, BuildTool.MAVEN, ["agorapulse-gru-http"], ApplicationType.DEFAULT, testFramework)
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
    void "test gradle agorapulse-gru-http with #language and #testFramework"(BuildTool buildTool, Language language, TestFramework testFramework) {
        when:
        generateProject(language, buildTool, ["agorapulse-gru-http"], ApplicationType.DEFAULT, testFramework)
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
