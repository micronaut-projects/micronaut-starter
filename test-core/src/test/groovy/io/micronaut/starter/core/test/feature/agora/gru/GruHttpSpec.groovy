package io.micronaut.starter.core.test.feature.agora.gru

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Unroll

class GruHttpSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "gru-http"
    }

    @Unroll
    void "test maven gru-http with #language and #testFramework"(Language language, TestFramework testFramework) {
        when:
        generateProject(language, BuildTool.MAVEN, ["gru-http"], ApplicationType.DEFAULT, testFramework)
        String output = executeMaven("compile test")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        [language, testFramework] << [
                Language.values(),
                (TestFramework.values() - TestFramework.KOTEST)
        ].combinations()
    }

    @Unroll
    void "test gradle gru-http with #language and #testFramework"(BuildTool buildTool, Language language, TestFramework testFramework) {
        when:
        generateProject(language, buildTool, ["gru-http"], ApplicationType.DEFAULT, testFramework)
        BuildResult result = executeGradle("test")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [buildTool, language, testFramework] << [
                [BuildTool.GRADLE_KOTLIN, BuildTool.GRADLE_KOTLIN],
                Language.values(),
                (TestFramework.values() - TestFramework.KOTEST)
        ].combinations()
    }
}
