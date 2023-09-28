package io.micronaut.starter.core.test.feature.database

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf

class DataMongoReactiveSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "dataMongodbReactive"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven data-mongodb-reactive with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, ["data-mongodb-reactive"])
        String output = executeMaven("compile test")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    void "test #buildTool data-mongodb-reactive with #language"(BuildTool buildTool, Language language) {
        when:
        generateProject(language, buildTool, ["data-mongodb-reactive"])
        BuildResult result = executeGradle("test")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [language, buildTool] << LanguageBuildCombinations.gradleCombinations()
    }
}
