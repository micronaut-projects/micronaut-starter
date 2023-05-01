package io.micronaut.starter.core.test.feature.database

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Ignore
import spock.lang.IgnoreIf
import spock.lang.Unroll

class DataMongoReactiveSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "dataMongodbReactive"
    }

    @Ignore('An exception java.lang.IncompatibleClassChangeError: class io.micronaut.http.filter.GenericHttpFilter$TerminalWithReactorContext cannot extend sealed interface io.micronaut.http.filter.GenericHttpFilter [enable DEBUG level for full stacktrace] was thrown by a user handler exceptionCaught() method while handling the following exception: java.lang.NoSuchFieldError: ROUTE')
    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    @Unroll
    void "test maven data-mongodb-reactive with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, ["data-mongodb-reactive"])
        String output = executeMaven("compile test")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    @Unroll
    void "test gradle data-mongodb-reactive with #language"(Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, ["data-mongodb-reactive"])
        BuildResult result = executeGradle("test")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }
}
