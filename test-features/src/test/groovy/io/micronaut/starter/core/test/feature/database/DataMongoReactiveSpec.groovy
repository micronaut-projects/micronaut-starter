package io.micronaut.starter.core.test.feature.database

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Unroll

class DataMongoReactiveSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "dataMongodbReactive"
    }

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
