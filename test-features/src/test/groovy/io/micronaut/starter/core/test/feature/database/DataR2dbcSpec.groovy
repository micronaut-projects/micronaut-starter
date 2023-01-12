package io.micronaut.starter.core.test.feature.database

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Unroll

class DataR2dbcSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "dataR2dbc"
    }

    @Unroll
    void "test maven data-r2dbc with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, ["data-r2dbc", "mysql"])
        String output = executeMaven("compile test")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    @Unroll
    void "test gradle data-r2dbc with #language"(Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, ["data-r2dbc", "mysql"])
        BuildResult result = executeGradle("test")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }
}
