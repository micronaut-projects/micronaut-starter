package io.micronaut.starter.core.test.feature.views

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf

class RockerSpec  extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "rockerViews"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven views-rocker with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, ["views-rocker"])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    void "test #buildTool views-rocker with #language"(BuildTool buildTool, Language language) {
        when:
        generateProject(language, buildTool, ["views-rocker"])
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [language, buildTool] << LanguageBuildCombinations.gradleCombinations()
    }
}
