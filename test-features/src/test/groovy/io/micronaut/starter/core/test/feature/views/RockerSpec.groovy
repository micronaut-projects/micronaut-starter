package io.micronaut.starter.core.test.feature.views

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf
import spock.lang.Unroll

class RockerSpec  extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "rockerViews"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    @Unroll
    void "test maven views-rocker with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, ["views-rocker"])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    @Unroll
    void "test gradle views-rocker with #language"(Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, ["views-rocker"])
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }
}
