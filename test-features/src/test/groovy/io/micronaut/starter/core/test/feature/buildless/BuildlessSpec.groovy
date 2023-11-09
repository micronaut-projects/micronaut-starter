package io.micronaut.starter.core.test.feature.buildless

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Unroll

class BuildlessSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        return "buildless"
    }

    @Unroll
    void "test gradle buildless integration with #language"(Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, ["buildless"])
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }
}
