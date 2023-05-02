package io.micronaut.starter.core.test.feature.database

import io.micronaut.starter.feature.database.MySQL
import io.micronaut.starter.feature.database.TestContainers
import io.micronaut.starter.feature.database.r2dbc.R2dbc
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Ignore
import spock.lang.IgnoreIf

class R2dbcSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "r2dbc"
    }

    @Ignore('''IncompatibleClassChangeError: class io.micronaut.http.filter.GenericHttpFilter$TerminalWithReactorContext cannot extend sealed interface io.micronaut.http.filter.GenericHttpFilter' [enable DEBUG level for full stacktrace] was thrown by a user handler's exceptionCaught() method while handling the following exception: java.lang.NoSuchFieldError: ROUTE io.micronaut.http.server.RouteExecutor.setRouteAttributes(RouteExecutor.java:190)''')
    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven r2dbc with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [R2dbc.NAME, MySQL.NAME])
        String output = executeMaven("compile test")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven data-r2dbc with TestContainers"() {
        when:
        generateProject(Language.JAVA, BuildTool.MAVEN, [R2dbc.NAME, MySQL.NAME, TestContainers.NAME])
        String output = executeMaven("compile test")

        then:
        output?.contains("BUILD SUCCESS")
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

    void "test gradle data-r2dbc with TestContainers"() {
        when:
        generateProject(Language.JAVA, BuildTool.GRADLE, [R2dbc.NAME, MySQL.NAME])
        BuildResult result = executeGradle("test")

        then:
        result?.output?.contains("BUILD SUCCESS")
    }
}
