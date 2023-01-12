package io.micronaut.starter.core.test.feature.database

import io.micronaut.starter.feature.database.MySQL
import io.micronaut.starter.feature.database.r2dbc.DataR2dbc
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult

class DataR2dbcSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "dataR2dbc"
    }

    void "test maven data-r2dbc with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [DataR2dbc.NAME, MySQL.NAME])
        String output = executeMaven("compile test")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    void "test gradle data-r2dbc with #language"(Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, [DataR2dbc.NAME, MySQL.NAME])
        BuildResult result = executeGradle("test")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }
}
