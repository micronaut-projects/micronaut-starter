package io.micronaut.starter.core.test.feature.database

import io.micronaut.starter.feature.database.MySQL
import io.micronaut.starter.feature.database.r2dbc.R2dbc
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult

class R2dbcSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "r2dbc"
    }

    void "test maven r2dbc with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [R2dbc.NAME, MySQL.NAME])
        String output = executeMaven("compile test")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    void "test gradle r2dbc with #language"(Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, [R2dbc.NAME, MySQL.NAME])
        BuildResult result = executeGradle("test")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }
}
